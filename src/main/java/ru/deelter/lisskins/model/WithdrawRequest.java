package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

/**
 * Request to withdraw skins from a specific purchase.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WithdrawRequest {

	/**
	 * Custom order identifier.
	 */
	@Nullable
	private String customId;

	/**
	 * Internal purchase identifier.
	 */
	@Nullable
	private Integer purchaseId;

	/**
	 * "partner" value from Steam Trade URL.
	 */
	@Nullable
	private String partner;

	/**
	 * "token" value from Steam Trade URL.
	 */
	@Nullable
	private String token;
}