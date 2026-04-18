package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Запрос на покупку скинов.
 *
 * <pre>{@code
 * {
 *   "ids": [125345],
 *   "partner": "123456789",
 *   "token": "abcdef",
 *   "max_price": 2.50,
 *   "custom_id": "my-order-123",
 *   "skip_unavailable": true
 * }
 * }</pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyRequest {

    /** ID скинов (максимум 100). */
    private List<Integer> ids;

    /** Значение "partner" из Trade URL Steam. */
    private String partner;

    /** Значение "token" из Trade URL Steam. */
    private String token;

    /** Максимальная цена покупки (опционально). */
    private Double maxPrice;

    /** Пользовательский ID для предотвращения двойных покупок. */
    private String customId;

    /** Пропускать недоступные скины при массовой покупке. */
    private Boolean skipUnavailable;
}