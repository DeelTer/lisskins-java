package ru.deelter.lisskins.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsTokenResponse {
    private TokenData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenData {
        private String token;
    }
}