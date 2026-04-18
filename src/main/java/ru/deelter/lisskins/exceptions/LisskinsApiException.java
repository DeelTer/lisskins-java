package ru.deelter.lisskins.exceptions;

import java.io.IOException;
import lombok.Getter;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;

@Getter
public class LisskinsApiException extends RuntimeException {

    private final int code;
    private final String errorBody;

    public LisskinsApiException(String message) {
        super(message);
        this.code = 0;
        this.errorBody = null;
    }

    public LisskinsApiException(Response<?> response) {
        super(buildMessage(response));
        this.code = response.code();
        this.errorBody = extractErrorBody(response);
    }

    public LisskinsApiException(Throwable cause) {
        super(cause);
        this.code = 0;
        this.errorBody = null;
    }

    @NotNull
    private static String buildMessage(@NotNull Response<?> response) {
        return "LIS-SKINS API error: " + response.code() + " " + response.message();
    }

    @Nullable
    private String extractErrorBody(Response<?> response) {
        try (ResponseBody body = response.errorBody()) {
            return body != null ? body.string() : null;
        } catch (IOException e) {
            return "Failed to read error body";
        }
    }
}
