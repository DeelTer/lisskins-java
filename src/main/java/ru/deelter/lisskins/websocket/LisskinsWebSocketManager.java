package ru.deelter.lisskins.websocket;

import io.github.centrifugal.centrifuge.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import ru.deelter.lisskins.LisskinsClient;
import ru.deelter.lisskins.model.WsTokenResponse;

import java.util.function.Consumer;

/**
 * Менеджер WebSocket соединений через Centrifugo.
 */
@Slf4j
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

	public void connect() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

		EventListener eventListener =
				new EventListener() {
					@Override
					public void onConnecting(Client client, @NotNull ConnectingEvent event) {
						log.info("Centrifugo: connecting... {}", event.getReason());
					}

					@Override
					public void onConnected(Client client, @NotNull ConnectedEvent event) {
						log.info("Centrifugo: connected (client id: {})", event.getClient());
						subscribeToChannels();
					}

					@Override
					public void onDisconnected(Client client, @NotNull DisconnectedEvent event) {
						log.info("Centrifugo: disconnected: {}", event.getReason());
					}

					@Override
					public void onError(Client client, @NotNull ErrorEvent event) {
						log.error("Centrifugo error", event.getError());
					}
				};

		Options options = new Options();
		options.setOkHttpClient(okHttpClient);
		centrifugeClient =
				new Client("wss://ws.lis-skins.com/connection/websocket", options, eventListener);

		client.getWsTokenAsync()
				.thenAccept(this::onTokenReceived)
				.exceptionally(
						ex -> {
							log.error("Failed to get WebSocket token: {}", ex.getMessage());
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
			obtainedSkinsSub =
					centrifugeClient.newSubscription(
							"public:obtained-skins",
							new SubscriptionEventListener() {
								@Override
								public void onPublication(
										Subscription sub, PublicationEvent event) {
									String data = new String(event.getData());
									log.debug("New skin event: {}", data);
									if (onObtainedSkinEvent != null) {
										onObtainedSkinEvent.accept(data);
									}
								}
							});
			obtainedSkinsSub.subscribe();

			String channel = "private:purchase-skins#" + userId;
			purchaseSkinsSub =
					centrifugeClient.newSubscription(
							channel,
							new SubscriptionEventListener() {
								@Override
								public void onPublication(
										Subscription sub, PublicationEvent event) {
									String data = new String(event.getData());
									log.debug("Purchase update: {}", data);
									if (onPurchaseUpdateEvent != null) {
										onPurchaseUpdateEvent.accept(data);
									}
								}
							});
			purchaseSkinsSub.subscribe();
		} catch (DuplicateSubscriptionException e) {
			log.error("Duplicate subscription: {}", e.getMessage());
		}
	}

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
