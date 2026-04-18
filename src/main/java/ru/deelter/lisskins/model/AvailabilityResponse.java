package ru.deelter.lisskins.model;

import java.util.List;
import java.util.Map;
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
public class AvailabilityResponse {

    private AvailabilityData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilityData {
        /** Скины, которые доступны для покупки + их актуальная цена */
        private Map<String, Double> availableSkins;

        /** ID скинов, которые недоступны для покупки */
        private List<Integer> unavailableSkinIds;
    }
}
