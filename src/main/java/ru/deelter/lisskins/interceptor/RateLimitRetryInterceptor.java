package ru.deelter.lisskins.interceptor;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Перехватчик для автоматического повторения запросов при получении 429 (Rate Limit).
 * Учитывает заголовок {@code retry-after} и делает до указанного числа попыток.
 */
public class RateLimitRetryInterceptor implements Interceptor {

    private final int maxRetries;

    public RateLimitRetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

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
                long waitSeconds = retryAfter != null ? Long.parseLong(retryAfter) : (attempt + 1) * 2L;

                System.out.println("[RateLimit] 429 получен. Ждём " + waitSeconds + " сек. Попытка " + (attempt + 1) + "/" + maxRetries);

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