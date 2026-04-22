package ru.deelter.lisskins.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.deelter.lisskins.model.*;

import java.util.List;
import java.util.Map;

/**
 * Retrofit interface defining LIS-SKINS API endpoints.
 */
public interface LisskinsApi {

	@GET("user/balance")
	Call<BalanceResponse> getBalance();

	@POST("market/buy")
	Call<BuyResponse> buy(@Body BuyRequest request);

	@GET("market/search")
	Call<SearchResponse> search(
			@Query("game") String game,
			@Query("price_from") Double priceFrom,
			@Query("price_to") Double priceTo,
			@Query("float_from") Double floatFrom,
			@Query("float_to") Double floatTo,
			@Query("only_unlocked") Integer onlyUnlocked,
			@Query("sort_by") String sortBy,
			@Query("cursor") String cursor,
			@Query("names[]") List<String> names,
			@Query("unlock_days[]") List<Integer> unlockDays,
			@Query("per_page") Integer perPage);

	@GET("market/info")
	Call<InfoResponse> getInfo(
			@Query("custom_ids[]") List<String> customIds,
			@Query("purchase_ids[]") List<Integer> purchaseIds);

	@GET("market/history")
	Call<HistoryResponse> getHistory(
			@Query("page") Integer page,
			@Query("start_unix_time") Long startTime,
			@Query("end_unix_time") Long endTime);

	@POST("market/withdraw-all")
	Call<WithdrawResponse> withdrawAll(@Body WithdrawAllRequest request);

	@POST("market/withdraw")
	Call<WithdrawResponse> withdraw(@Body WithdrawRequest request);

	@POST("market/return")
	Call<ReturnResponse> returnSkin(@Body Map<String, Object> body);

	@GET("market/check-availability")
	Call<AvailabilityResponse> checkAvailability(@Query("ids[]") List<Integer> ids);

	@GET("user/get-ws-token")
	Call<WsTokenResponse> getWsToken();
}