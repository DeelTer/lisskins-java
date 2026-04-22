package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Search request parameters.
 */
@Getter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest {

	/**
	 * Game identifier (csgo, dota2, rust).
	 */
	private String game;

	/**
	 * Minimum price filter.
	 */
	private Double priceFrom;

	/**
	 * Maximum price filter.
	 */
	private Double priceTo;

	/**
	 * Minimum float value filter (CS2 only).
	 */
	private Double floatFrom;

	/**
	 * Maximum float value filter (CS2 only).
	 */
	private Double floatTo;

	/**
	 * List of skin names to search for.
	 */
	private List<String> names;

	/**
	 * Show only unlocked skins (1) or all (0).
	 */
	private Integer onlyUnlocked;

	/**
	 * Filter by unlock days remaining.
	 */
	private List<Integer> unlockDays;

	/**
	 * Sort order (oldest, newest, lowest_price, highest_price).
	 */
	private String sortBy;

	/**
	 * Pagination cursor for the next page.
	 */
	private String cursor;

	/**
	 * Number of items per page (default is 200).
	 */
	private Integer perPage;
}