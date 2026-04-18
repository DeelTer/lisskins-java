package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityResponse {

    private AvailabilityData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilityData {
        /**
         * Скины, которые доступны для покупки + их актуальная цена
         */
        private Map<String, Double> availableSkins;

        /**
         * ID скинов, которые недоступны для покупки
         */
        private List<Integer> unavailableSkinIds;
    }
}