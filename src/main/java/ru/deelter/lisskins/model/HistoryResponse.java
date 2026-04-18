package ru.deelter.lisskins.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private List<Purchase> data;
    private HistoryMeta meta;

    @Data
    @Builder
    @Jacksonized
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryMeta {
        private Integer currentPage;
        private Integer lastPage;
        private Integer perPage;
        private Integer total;
    }
}
