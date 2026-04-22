package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Request to withdraw all available skins.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WithdrawAllRequest {

	/**
	 * Custom order identifiers.
	 */
	@Nullable
	private List<String> customIds;

	/**
	 * Internal purchase identifiers.
	 */
	@Nullable
	private List<Integer> purchaseIds;

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