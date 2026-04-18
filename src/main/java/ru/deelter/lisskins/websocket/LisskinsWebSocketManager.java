package ru.deelter.lisskins.websocket;

import io.github.centrifugal.centrifuge.*;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import ru.deelter.lisskins.LisskinsClient;
import ru.deelter.lisskins.model.WsTokenResponse;

import java.util.function.Consumer;

/**
 * Менеджер WebSocket соединений через Centrifugo.
 * <p>
 * Позволяет подписываться на события новых скинов и изменения статусов покупок.
 * Для использования необходимо передать {@code userId} при создании клиента.
 * </p>
 *
 * <pre>{@code
 * LisskinsClient client = LisskinsClient.builder()
 *         .apiKey("...")
 *         .userId("12345")
 *         .build();
 * LisskinsWebSocketManager ws = client.getWebSocketManager();
 * ws.setOnObtainedSkinListener(json -> System.out.println(json));
 * ws.connect(); // userId уже известен
 * }</pre>
 */
public class LisskinsWebSocketManager {

	private final LisskinsClient client;
	private final String userId;
	private Client centrifugeClient;
	private Subscription obtainedSkinsSub;
	private Subscription purchaseSkinsSub;

	private Consumer<String> onObtainedSkinEvent;
	private Consumer<String> onPurchaseUpdateEvent;

	public LisskinsWebSocketManager(LisskinsClient client, String userId) {
		this.client = client;
		this.userId = userId;
	}

	public void setOnObtainedSkinListener(Consumer<String> listener) {
		this.onObtainedSkinEvent = listener;
	}

	public void setOnPurchaseUpdateListener(Consumer<String> listener) {
		this.onPurchaseUpdateEvent = listener;
	}

	/**
	 * Подключается к WebSocket, используя ранее переданный userId.
	 */
	public void connect() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

		EventListener eventListener = new EventListener() {
			@Override
			public void onConnecting(Client client, @NotNull ConnectingEvent event) {
				System.out.println("Centrifugo: connecting... " + event.getReason());
			}

			@Override
			public void onConnected(Client client, @NotNull ConnectedEvent event) {
				System.out.println("Centrifugo: connected (client id: " + event.getClient() + ")");
				subscribeToChannels();
			}

			@Override
			public void onDisconnected(Client client, @NotNull DisconnectedEvent event) {
				System.out.println("Centrifugo: disconnected: " + event.getReason());
			}

			@Override
			public void onError(Client client, @NotNull ErrorEvent event) {
				System.err.println("Centrifugo error: " + event.getError());
			}
		};

		Options options = new Options();
		options.setOkHttpClient(okHttpClient);
		centrifugeClient = new Client("wss://ws.lis-skins.com/connection/websocket", options, eventListener);

		client.getWsTokenAsync()
				.thenAccept(this::onTokenReceived)
				.exceptionally(ex -> {
					System.err.println("Failed to get WebSocket token: " + ex.getMessage());
					return null;
				});
	}

	private void onTokenReceived(@NotNull WsTokenResponse tokenResponse) {
		String token = tokenResponse.getData().getToken();
		centrifugeClient.setToken(token);
		centrifugeClient.connect();
	}

	private void subscribeToChannels() {
		try {
			obtainedSkinsSub = centrifugeClient.newSubscription(
					"public:obtained-skins",
					new SubscriptionEventListener() {
						@Override
						public void onPublication(Subscription sub, PublicationEvent event) {
							String data = new String(event.getData());
							System.out.println("New skin event: " + data);
							if (onObtainedSkinEvent != null) {
								onObtainedSkinEvent.accept(data);
							}
						}
					}
			);
			obtainedSkinsSub.subscribe();

			String channel = "private:purchase-skins#" + userId;
			purchaseSkinsSub = centrifugeClient.newSubscription(
					channel,
					new SubscriptionEventListener() {
						@Override
						public void onPublication(Subscription sub, PublicationEvent event) {
							String data = new String(event.getData());
							System.out.println("Purchase update: " + data);
							if (onPurchaseUpdateEvent != null) {
								onPurchaseUpdateEvent.accept(data);
							}
						}
					}
			);
			purchaseSkinsSub.subscribe();
		} catch (DuplicateSubscriptionException e) {
			System.err.println("Duplicate subscription: " + e.getMessage());
		}
	}

	/**
	 * Отключает WebSocket соединение.
	 */
	public void disconnect() {
		if (centrifugeClient != null) {
			centrifugeClient.disconnect();
		}
	}

	public void unsubscribeFromObtainedSkins() {
		if (obtainedSkinsSub != null) {
			obtainedSkinsSub.unsubscribe();
		}
	}

	public void unsubscribeFromPurchasedSkins() {
		if (purchaseSkinsSub != null) {
			purchaseSkinsSub.unsubscribe();
		}
	}
}