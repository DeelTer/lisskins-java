package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

/**
 * Information about an individual skin within a purchase.
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PurchaseSkin {

	/**
	 * Skin identifier.
	 */
	private Integer id;

	/**
	 * Skin name.
	 */
	private String name;

	/**
	 * Purchase price of the skin.
	 */
	private Double price;

	/**
	 * Current status of the skin (see {@link ru.deelter.lisskins.constants.ApiConstants.PurchaseStatus}).
	 */
	private String status;

	/**
	 * Reason for return (if status is "return").
	 */
	private String returnReason;

	/**
	 * Commission charged upon return.
	 */
	private Double returnChargedCommission;

	/**
	 * Error message if an error occurred during processing.
	 */
	private String error;

	/**
	 * Steam trade offer ID.
	 */
	private String steamTradeOfferId;

	/**
	 * Date when the trade offer was created.
	 */
	private Instant steamTradeOfferCreatedAt;

	/**
	 * Date when the trade offer expires.
	 */
	private Instant steamTradeOfferExpiryAt;

	/**
	 * Date when the trade offer was completed (accepted/declined/cancelled).
	 */
	private Instant steamTradeOfferFinishedAt;
}