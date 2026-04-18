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
public class Gem {
    private List<String> identifiers;
    private List<String> imageUrls;
    private String name;
    private String description;
    private String color;
}
