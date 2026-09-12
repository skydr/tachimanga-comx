package eu.kanade.tachiyomi.extension.ru.comx

import okhttp3.Interceptor
import okhttp3.Response

class BackoffInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())
        var attempt = 0
        while (response.code == 429 || response.code == 503) {
            if (attempt++ >= 2) break
            val retryAfter = response.header("Retry-After")?.toLongOrNull()
            val delayMs = when {
                retryAfter != null -> retryAfter.coerceIn(1L, 30L) * 1000
                attempt == 1 -> 2_000L
                else -> 5_000L
            }
            response.close()
            Thread.sleep(delayMs)
            response = chain.proceed(chain.request())
        }
        return response
    }
}
