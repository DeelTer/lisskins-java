package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WsTokenResponse {
    private TokenData data;

    @Data
    @Builder
    @Jacksonized
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenData {
        private String token;
    }
}
