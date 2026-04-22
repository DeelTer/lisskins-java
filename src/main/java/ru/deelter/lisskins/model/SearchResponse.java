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
 * Response for a skin search request.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchResponse {

	/**
	 * List of found skins.
	 */
	private List<Skin> data;

	/**
	 * Pagination metadata.
	 */
	private SearchMeta meta;

	/**
	 * Pagination metadata.
	 */
	@Data
	@Builder
	@Jacksonized
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchMeta {

		/**
		 * Cursor for retrieving the next page of results.
		 */
		private String nextCursor;

		/**
		 * Number of items per page.
		 */
		private Integer perPage;
	}
}