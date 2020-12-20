package com.four.ds_weather.net

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 域名拦截器，应该放在AppInterceptor
 */
class HostInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        return if (oldRequest.headers["myHost"] != null) {
            val host = oldRequest.headers["myHost"]!!
            if (oldRequest.url.host != host) {
                val newRequest = Request.Builder()
                    .method(oldRequest.method, oldRequest.body)
                    .cacheControl(oldRequest.cacheControl)
                    .headers(oldRequest.headers)
                    .url(oldRequest.url)
                    .build()
                chain.proceed(newRequest)
            }
            chain.proceed(oldRequest)
        } else {
            chain.proceed(oldRequest)
        }
    }
}