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
import java.util.Map;

/**
 * Skin model with complete information.
 *
 * <pre>{@code
 * {
 *   "id": 125346,
 *   "name": "AWP | Phobos (Minimal Wear)",
 *   "price": 0.83,
 *   "unlock_at": null,
 *   "item_class_id": "6026847480",
 *   "created_at": "2024-07-22T11:31:01.000000Z",
 *   "item_float": "0.123551741242409",
 *   "name_tag": null,
 *   "item_paint_index": null,
 *   "item_paint_seed": null,
 *   "stickers": [ ... ],
 *   "gems": [ ... ],
 *   "styles": { "Default Style": false },
 *   "item_asset_id": "123",
 *   "game_id": 1,
 *   "item_link": "steam://run/730/..."
 * }
 * }</pre>
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Skin {

	/**
	 * Unique skin identifier (used for purchase).
	 */
	private Integer id;

	/**
	 * Skin name.
	 */
	private String name;

	/**
	 * Current price in USD.
	 */
	private Double price;

	/**
	 * Date and time when the skin becomes available for withdrawal (if trade locked).
	 */
	private Instant unlockAt;

	/**
	 * Class ID from Steam inventory. Can be used to retrieve the skin image.
	 */
	private String itemClassId;

	/**
	 * Date the skin was added to the database.
	 */
	private Instant createdAt;

	/**
	 * Float value of the skin (CS2 only).
	 */
	private String itemFloat;

	/**
	 * Name tag of the skin (CS2 only).
	 */
	private String nameTag;

	/**
	 * Paint index (CS2 only).
	 */
	private Integer itemPaintIndex;

	/**
	 * Paint seed (CS2 only).
	 */
	private Integer itemPaintSeed;

	/**
	 * List of stickers applied to the skin (CS2 only).
	 */
	private List<Sticker> stickers;

	/**
	 * List of gems socketed into the skin (Dota 2 only).
	 */
	private List<Gem> gems;

	/**
	 * Skin styles (Dota 2 only).
	 * Key — style name, value — {@code true} if the style is unlocked.
	 */
	private Map<String, Boolean> styles;

	/**
	 * Asset ID of the item.
	 */
	private String itemAssetId;

	/**
	 * Game identifier (1 – CS2, 2 – Dota 2, 3 – Rust).
	 */
	private Integer gameId;

	/**
	 * Inspect link for the item in-game (may not always be available).
	 */
	private String itemLink;
}