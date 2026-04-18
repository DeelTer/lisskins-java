package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sticker {
    private String name;
    private String image;
    private Integer wear;
    private Integer slot;
}