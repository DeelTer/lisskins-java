package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

/**
 * Response for a skin availability check request.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AvailabilityResponse {

	private AvailabilityData data;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class AvailabilityData {

		/**
		 * Skins available for purchase.
		 * Key — string representation of skin ID, value — current price.
		 */
		private Map<String, Double> availableSkins;

		/**
		 * List of skin IDs that are unavailable for purchase.
		 */
		private List<Integer> unavailableSkinIds;
	}
}