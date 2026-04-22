package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;

/**
 * Purchase information.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Purchase {

	/**
	 * Internal purchase identifier.
	 */
	private Integer purchaseId;

	/**
	 * Steam ID of the buyer.
	 */
	private String steamId;

	/**
	 * Date and time when the purchase was created.
	 */
	private Instant createdAt;

	/**
	 * Custom identifier provided when creating the order.
	 */
	private String customId;

	/**
	 * List of skins purchased in this transaction.
	 */
	private List<PurchaseSkin> skins;
}