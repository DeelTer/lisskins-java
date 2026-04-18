package ru.deelter.lisskins;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.deelter.lisskins.api.LisskinsApi;
import ru.deelter.lisskins.exceptions.LisskinsApiException;
import ru.deelter.lisskins.interceptor.RateLimitRetryInterceptor;
import ru.deelter.lisskins.model.*;
import ru.deelter.lisskins.websocket.LisskinsWebSocketManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Основной клиент для работы с LIS-SKINS API.
 *
 * <p>Предоставляет синхронные и асинхронные методы для всех операций: получение баланса, покупка
 * скинов, поиск, проверка доступности, получение информации о покупках, история, вывод средств и
 * возврат.
 *
 * <p>WebSocket-соединение включается опционально: передайте {@code userId} в билдер.
 *
 * <pre>{@code
 * LisskinsClient client = LisskinsClient.builder()
 *         .apiKey("your-api-key")
 *         .debug(true)
 *         .userId("12345") // для WebSocket
 *         .build();
 * }</pre>
 */
@Getter
@Slf4j
@ToString(exclude = "apiKey")
public class LisskinsClient {

	private final LisskinsApi api;
	private final LisskinsWebSocketManager webSocketManager;
	private final String apiKey;

	@Builder
	public LisskinsClient(String apiKey, boolean debug, @Nullable String userId) {
		if (apiKey == null || apiKey.isBlank()) {
			throw new IllegalArgumentException("API Key обязателен");
		}
		this.apiKey = apiKey;

		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(
				debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

		OkHttpClient okHttpClient =
				new OkHttpClient.Builder()
						.addInterceptor(logging)
						.addInterceptor(
								chain ->
										chain.proceed(
												chain.request()
														.newBuilder()
														.addHeader(
																"Authorization", "Bearer " + apiKey)
														.build()))
						.addInterceptor(new RateLimitRetryInterceptor(3))
						.connectTimeout(30, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.build();

		ObjectMapper mapper = new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Retrofit retrofit =
				new Retrofit.Builder()
						.baseUrl("https://api.lis-skins.com/v1/")
						.client(okHttpClient)
						.addConverterFactory(JacksonConverterFactory.create(mapper))
						.build();

		this.api = retrofit.create(LisskinsApi.class);
		this.webSocketManager =
				(userId != null && !userId.isBlank())
						? new LisskinsWebSocketManager(this, userId)
						: null;
	}

	/**
	 * Возвращает текущий баланс пользователя.
	 */
	public BalanceResponse getBalance() {
		return executeSync(api.getBalance());
	}

	/**
	 * Покупка одного или нескольких скинов.
	 *
	 * @param request параметры покупки
	 */
	public BuyResponse buy(BuyRequest request) {
		return executeSync(api.buy(request));
	}

	/**
	 * Поиск скинов по заданным критериям.
	 *
	 * @param request параметры поиска
	 */
	public SearchResponse search(@NotNull SearchRequest request) {
		return executeSync(
				api.search(
						request.getGame(),
						request.getPriceFrom(),
						request.getPriceTo(),
						request.getFloatFrom(),
						request.getFloatTo(),
						request.getOnlyUnlocked(),
						request.getSortBy(),
						request.getCursor(),
						request.getNames(),
						request.getUnlockDays()));
	}

	/**
	 * Проверяет доступность списка скинов по их ID.
	 */
	public AvailabilityResponse checkAvailability(List<Integer> ids) {
		return executeSync(api.checkAvailability(ids));
	}

	/**
	 * Получает информацию о покупках по custom_id или purchase_id.
	 */
	public InfoResponse getInfo(
			@Nullable List<String> customIds, @Nullable List<Integer> purchaseIds) {
		return executeSync(api.getInfo(customIds, purchaseIds));
	}

	/**
	 * История покупок с пагинацией.
	 */
	public HistoryResponse getHistory(
			@Nullable Integer page, @Nullable Long startUnixTime, @Nullable Long endUnixTime) {
		return executeSync(api.getHistory(page, startUnixTime, endUnixTime));
	}

	/**
	 * Вывод всех разблокированных скинов (можно указать конкретные покупки).
	 */
	public WithdrawResponse withdrawAll(
			@Nullable List<String> customIds,
			@Nullable List<Integer> purchaseIds,
			@Nullable String partner,
			@Nullable String token) {
		Map<String, Object> body = new HashMap<>();
		if (customIds != null) body.put("custom_ids[]", customIds);
		if (purchaseIds != null) body.put("purchase_ids[]", purchaseIds);
		if (partner != null) body.put("partner", partner);
		if (token != null) body.put("token", token);
		return executeSync(api.withdrawAll(body));
	}

	/**
	 * Вывод скинов из конкретной покупки.
	 */
	public WithdrawResponse withdraw(
			@Nullable String customId,
			@Nullable Integer purchaseId,
			@Nullable String partner,
			@Nullable String token) {
		Map<String, Object> body = new HashMap<>();
		if (customId != null) body.put("custom_id", customId);
		if (purchaseId != null) body.put("purchase_id", purchaseId);
		if (partner != null) body.put("partner", partner);
		if (token != null) body.put("token", token);
		return executeSync(api.withdraw(body));
	}

	/**
	 * Возврат заблокированного скина (с комиссией 3%).
	 */
	public ReturnResponse returnSkin(
			@Nullable String customId, @Nullable Integer purchaseId, @Nullable Integer skinId) {
		Map<String, Object> body = new HashMap<>();
		if (customId != null) body.put("custom_id", customId);
		if (purchaseId != null) body.put("purchase_id", purchaseId);
		if (skinId != null) body.put("id", skinId);
		return executeSync(api.returnSkin(body));
	}

	/**
	 * Получение токена для WebSocket соединения.
	 */
	public WsTokenResponse getWsToken() {
		return executeSync(api.getWsToken());
	}

	// Асинхронные методы
	public CompletableFuture<BalanceResponse> getBalanceAsync() {
		return executeAsync(api.getBalance());
	}

	public CompletableFuture<BuyResponse> buyAsync(BuyRequest request) {
		return executeAsync(api.buy(request));
	}

	public CompletableFuture<SearchResponse> searchAsync(@NotNull SearchRequest request) {
		return executeAsync(
				api.search(
						request.getGame(),
						request.getPriceFrom(),
						request.getPriceTo(),
						request.getFloatFrom(),
						request.getFloatTo(),
						request.getOnlyUnlocked(),
						request.getSortBy(),
						request.getCursor(),
						request.getNames(),
						request.getUnlockDays()));
	}

	public CompletableFuture<AvailabilityResponse> checkAvailabilityAsync(List<Integer> ids) {
		return executeAsync(api.checkAvailability(ids));
	}

	public CompletableFuture<InfoResponse> getInfoAsync(
			@Nullable List<String> customIds, @Nullable List<Integer> purchaseIds) {
		return executeAsync(api.getInfo(customIds, purchaseIds));
	}

	public CompletableFuture<HistoryResponse> getHistoryAsync(
			@Nullable Integer page, @Nullable Long startUnixTime, @Nullable Long endUnixTime) {
		return executeAsync(api.getHistory(page, startUnixTime, endUnixTime));
	}

	public CompletableFuture<WsTokenResponse> getWsTokenAsync() {
		return executeAsync(api.getWsToken());
	}

	private <T> T executeSync(@NotNull Call<T> call) {
		try {
			var response = call.execute();
			if (!response.isSuccessful()) {
				throw new LisskinsApiException(response);
			}
			return response.body();
		} catch (IOException e) {
			log.error("API request failed", e);
			throw new LisskinsApiException(e);
		}
	}

	@NotNull
	private <T> CompletableFuture<T> executeAsync(@NotNull Call<T> call) {
		CompletableFuture<T> future = new CompletableFuture<>();
		call.enqueue(
				new retrofit2.Callback<T>() {
					@Override
					public void onResponse(
							@NotNull Call<T> call, @NotNull retrofit2.Response<T> response) {
						if (response.isSuccessful()) {
							future.complete(response.body());
						} else {
							future.completeExceptionally(new LisskinsApiException(response));
						}
					}

					@Override
					public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
						log.error("Async API request failed", t);
						future.completeExceptionally(new LisskinsApiException(t));
					}
				});
		return future;
	}
}
