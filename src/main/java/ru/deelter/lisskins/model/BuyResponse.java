package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ на запрос покупки.
 *
 * <pre>{@code
 * {
 *   "data": {
 *     "purchase_id": 55,
 *     "steam_id": "76561198050648523",
 *     "created_at": "2024-07-22T14:50:08.000000Z",
 *     "custom_id": null,
 *     "skins": [ ... ]
 *   }
 * }
 * }</pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyResponse {
    private Purchase data;
}