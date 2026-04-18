package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ответ на запрос поиска скинов.
 *
 * <pre>{@code
 * {
 *   "data": [ ... ],
 *   "meta": {
 *     "per_page": 200,
 *     "next_cursor": "eyJvYnRhaW5lZF9za2lucy5pZCI6NDY5MjAy..."
 *   }
 * }
 * }</pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private List<Skin> data;
    private SearchMeta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchMeta {
        private String nextCursor;
        private Integer perPage;
    }
}