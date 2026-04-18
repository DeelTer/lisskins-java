package ru.deelter.lisskins.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Модель скина с полной информацией.
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
public class Skin {
    private Integer id;
    private String name;
    private Double price;
    private Instant unlockAt;
    private String itemClassId;
    private Instant createdAt;
    private String itemFloat;
    private String nameTag;
    private Integer itemPaintIndex;
    private Integer itemPaintSeed;
    private List<Sticker> stickers;
    private List<Gem> gems;
    private Map<String, Boolean> styles;
    private String itemAssetId;
    private Integer gameId;
    private String itemLink;
}
