package ru.deelter.lisskins.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * Response containing a WebSocket connection token.
 */
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

		/**
		 * Token for Centrifugo authentication.
		 */
		private String token;
	}
}