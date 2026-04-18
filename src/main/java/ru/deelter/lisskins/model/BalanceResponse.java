package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Ответ на запрос баланса.
 *
 * <pre>{@code
 * {
 *   "data": {
 *     "balance": 99.96
 *   }
 * }
 * }</pre>
 */
@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class BalanceResponse {

    private Balance data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Balance {
        private Double balance;
    }
}
