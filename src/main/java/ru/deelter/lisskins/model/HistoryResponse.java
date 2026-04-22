package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response containing purchase history.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HistoryResponse {

	/**
	 * List of purchases.
	 */
	private List<Purchase> data;

	/**
	 * Pagination metadata.
	 */
	private HistoryMeta meta;

	@Data
	@Builder
	@Jacksonized
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	public static class HistoryMeta {

		/**
		 * Current page number.
		 */
		private Integer currentPage;

		/**
		 * Last available page number.
		 */
		private Integer lastPage;

		/**
		 * Number of items per page.
		 */
		private Integer perPage;

		/**
		 * Total number of records.
		 */
		private Integer total;
	}
}