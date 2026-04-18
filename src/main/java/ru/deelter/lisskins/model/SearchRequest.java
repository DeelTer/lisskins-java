package ru.deelter.lisskins.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
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