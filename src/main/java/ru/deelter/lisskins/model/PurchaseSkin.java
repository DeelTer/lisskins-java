package ru.deelter.lisskins.model;

import java.time.Instant;
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
public class PurchaseSkin {
    private Integer id;
    private String name;
    private Double price;
    private String status;
    private String returnReason;
    private Double returnChargedCommission;
    private String error;
    private String steamTradeOfferId;
    private Instant steamTradeOfferCreatedAt;
    private Instant steamTradeOfferExpiryAt;
    private Instant steamTradeOfferFinishedAt;
}
