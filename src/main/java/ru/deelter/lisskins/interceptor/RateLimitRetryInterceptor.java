package ru.deelter.lisskins.interceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/**
 * Перехватчик для автоматического повторения запросов при получении 429 (Rate Limit). Учитывает
 * заголовок {@code retry-after} и делает до указанного числа попыток.
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitRetryInterceptor implements Interceptor {

    int maxRetries;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                Response response = chain.proceed(chain.request());

                if (response.code() != 429) {
                    return response;
                }

                String retryAfter = response.header("retry-after");
                long waitSeconds =
                        retryAfter != null ? Long.parseLong(retryAfter) : (attempt + 1) * 2L;

                log.warn(
                        "[RateLimit] 429 received. Waiting {} sec. Attempt {}/{}",
                        waitSeconds,
                        attempt + 1,
                        maxRetries);

                response.close();
                TimeUnit.SECONDS.sleep(waitSeconds);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Retry interrupted", e);
            }
        }
        return chain.proceed(chain.request());
    }
}
