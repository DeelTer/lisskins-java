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
 * Gem socketed into a skin (Dota 2).
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Gem {

	/**
	 * Gem identifiers.
	 */
	private List<String> identifiers;

	/**
	 * URLs of gem images.
	 */
	private List<String> imageUrls;

	/**
	 * Gem name.
	 */
	private String name;

	/**
	 * Gem description.
	 */
	private String description;

	/**
	 * Gem color in HEX format.
	 */
	private String color;
}