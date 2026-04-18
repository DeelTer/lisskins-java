package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private List<Purchase> data;
    private HistoryMeta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryMeta {
        private Integer currentPage;
        private Integer lastPage;
        private Integer perPage;
        private Integer total;
    }
}