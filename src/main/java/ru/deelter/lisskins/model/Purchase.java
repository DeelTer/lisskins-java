package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private Integer purchaseId;
    private String steamId;
    private Instant createdAt;
    private String customId;
    private List<PurchaseSkin> skins;
}