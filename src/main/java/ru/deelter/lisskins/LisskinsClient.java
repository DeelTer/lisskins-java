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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Main client for interacting with the LIS-SKINS API.
 *
 * <p>Provides synchronous and asynchronous methods for all operations: checking balance,
 * purchasing skins, searching, checking availability, retrieving purchase information,
 * history, withdrawals, and returns.
 *
 * <p>WebSocket connection is optional: provide {@code userId} in the builder to enable it.
 *
 * <pre>{@code
 * LisskinsClient client = LisskinsClient.builder()
 *         .apiKey("your-api-key")
 *         .debug(true)
 *         .userId("12345") // for WebSocket
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
			throw new IllegalArgumentException("API Key is required");
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
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

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
	 * Returns the current user balance.
	 *
	 * @return balance response
	 */
	public BalanceResponse getBalance() {
		return executeSync(api.getBalance());
	}

	/**
	 * Purchases one or more skins.
	 *
	 * @param request purchase parameters
	 * @return purchase response
	 */
	public BuyResponse buy(BuyRequest request) {
		return executeSync(api.buy(request));
	}

	/**
	 * Searches for skins based on specified criteria.
	 *
	 * @param request search parameters
	 * @return search response
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
						request.getUnlockDays(),
						request.getPerPage()));
	}

	/**
	 * Checks the availability of a list of skins by their IDs.
	 *
	 * @param ids list of skin IDs
	 * @return availability response
	 */
	public AvailabilityResponse checkAvailability(List<Integer> ids) {
		return executeSync(api.checkAvailability(ids));
	}

	/**
	 * Retrieves purchase information by custom_id or purchase_id.
	 *
	 * @param customIds   custom order identifiers (optional)
	 * @param purchaseIds internal purchase identifiers (optional)
	 * @return info response
	 */
	public InfoResponse getInfo(
			@Nullable List<String> customIds, @Nullable List<Integer> purchaseIds) {
		return executeSync(api.getInfo(customIds, purchaseIds));
	}

	/**
	 * Retrieves purchase history with pagination.
	 *
	 * @param page          page number (optional)
	 * @param startUnixTime start timestamp (optional)
	 * @param endUnixTime   end timestamp (optional)
	 * @return history response
	 */
	public HistoryResponse getHistory(
			@Nullable Integer page, @Nullable Long startUnixTime, @Nullable Long endUnixTime) {
		return executeSync(api.getHistory(page, startUnixTime, endUnixTime));
	}

	/**
	 * Withdraws all unlocked skins (specific purchases can be specified).
	 *
	 * @param customIds   custom order identifiers (optional)
	 * @param purchaseIds internal purchase identifiers (optional)
	 * @param partner     "partner" value from Steam Trade URL (optional)
	 * @param token       "token" value from Steam Trade URL (optional)
	 * @return withdraw response
	 */
	public WithdrawResponse withdrawAll(
			@Nullable List<String> customIds,
			@Nullable List<Integer> purchaseIds,
			@Nullable String partner,
			@Nullable String token) {
		WithdrawAllRequest request = WithdrawAllRequest.builder()
				.customIds(customIds)
				.purchaseIds(purchaseIds)
				.partner(partner)
				.token(token)
				.build();
		return executeSync(api.withdrawAll(request));
	}

	/**
	 * Withdraws skins from a specific purchase.
	 *
	 * @param customId   custom order identifier (optional)
	 * @param purchaseId internal purchase identifier (optional)
	 * @param partner    "partner" value from Steam Trade URL (optional)
	 * @param token      "token" value from Steam Trade URL (optional)
	 * @return withdraw response
	 */
	public WithdrawResponse withdraw(
			@Nullable String customId,
			@Nullable Integer purchaseId,
			@Nullable String partner,
			@Nullable String token) {
		WithdrawRequest request = WithdrawRequest.builder()
				.customId(customId)
				.purchaseId(purchaseId)
				.partner(partner)
				.token(token)
				.build();
		return executeSync(api.withdraw(request));
	}

	/**
	 * Returns a locked skin (3% commission applies).
	 *
	 * @param customId   custom order identifier (optional)
	 * @param purchaseId internal purchase identifier (optional)
	 * @param skinId     skin identifier (optional)
	 * @return return response
	 */
	public ReturnResponse returnSkin(
			@Nullable String customId, @Nullable Integer purchaseId, @Nullable Integer skinId) {
		Map<String, Object> body = new java.util.HashMap<>();
		if (customId != null) body.put("custom_id", customId);
		if (purchaseId != null) body.put("purchase_id", purchaseId);
		if (skinId != null) body.put("id", skinId);
		return executeSync(api.returnSkin(body));
	}

	/**
	 * Retrieves a token for WebSocket connection.
	 *
	 * @return WebSocket token response
	 */
	public WsTokenResponse getWsToken() {
		return executeSync(api.getWsToken());
	}

	// Asynchronous methods

	/**
	 * Asynchronously returns the current user balance.
	 *
	 * @return future with balance response
	 */
	public CompletableFuture<BalanceResponse> getBalanceAsync() {
		return executeAsync(api.getBalance());
	}

	/**
	 * Asynchronously purchases one or more skins.
	 *
	 * @param request purchase parameters
	 * @return future with purchase response
	 */
	public CompletableFuture<BuyResponse> buyAsync(BuyRequest request) {
		return executeAsync(api.buy(request));
	}

	/**
	 * Asynchronously searches for skins based on specified criteria.
	 *
	 * @param request search parameters
	 * @return future with search response
	 */
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
						request.getUnlockDays(),
						request.getPerPage()));
	}

	/**
	 * Asynchronously checks the availability of a list of skins by their IDs.
	 *
	 * @param ids list of skin IDs
	 * @return future with availability response
	 */
	public CompletableFuture<AvailabilityResponse> checkAvailabilityAsync(List<Integer> ids) {
		return executeAsync(api.checkAvailability(ids));
	}

	/**
	 * Asynchronously retrieves purchase information by custom_id or purchase_id.
	 *
	 * @param customIds   custom order identifiers (optional)
	 * @param purchaseIds internal purchase identifiers (optional)
	 * @return future with info response
	 */
	public CompletableFuture<InfoResponse> getInfoAsync(
			@Nullable List<String> customIds, @Nullable List<Integer> purchaseIds) {
		return executeAsync(api.getInfo(customIds, purchaseIds));
	}

	/**
	 * Asynchronously retrieves purchase history with pagination.
	 *
	 * @param page          page number (optional)
	 * @param startUnixTime start timestamp (optional)
	 * @param endUnixTime   end timestamp (optional)
	 * @return future with history response
	 */
	public CompletableFuture<HistoryResponse> getHistoryAsync(
			@Nullable Integer page, @Nullable Long startUnixTime, @Nullable Long endUnixTime) {
		return executeAsync(api.getHistory(page, startUnixTime, endUnixTime));
	}

	/**
	 * Asynchronously retrieves a token for WebSocket connection.
	 *
	 * @return future with WebSocket token response
	 */
	public CompletableFuture<WsTokenResponse> getWsTokenAsync() {
		return executeAsync(api.getWsToken());
	}

	private <T> T executeSync(@NotNull Call<T> call) {
		try {
			var response = call.execute();
			if (!response.isSuccessful()) {
				throw new LisskinsApiException(response);
			}
			T body = response.body();
			if (body == null) {
				throw new LisskinsApiException("Empty response body for successful request");
			}
			return body;
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
							T body = response.body();
							if (body == null) {
								future.completeExceptionally(
										new LisskinsApiException("Empty response body for successful request"));
							} else {
								future.complete(body);
							}
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