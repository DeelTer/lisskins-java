package ru.deelter.lisskins.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String game;
    private Double priceFrom;
    private Double priceTo;
    private Double floatFrom;
    private Double floatTo;
    private List<String> names;
    private Integer onlyUnlocked;
    private List<Integer> unlockDays;
    private String sortBy;
    private String cursor;
}
