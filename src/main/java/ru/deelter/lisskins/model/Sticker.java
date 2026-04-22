package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Sticker applied to a skin (CS2).
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Sticker {

	/**
	 * Sticker name.
	 */
	private String name;

	/**
	 * URL of the sticker image.
	 */
	private String image;

	/**
	 * Sticker wear (0–100).
	 */
	private Integer wear;

	/**
	 * Slot where the sticker is applied (0–4).
	 */
	private Integer slot;
}