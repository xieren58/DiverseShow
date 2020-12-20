package com.four.common_net.interceptors

import com.four.common_util.log.DSLog
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * 域名拦截器，应该放在AppInterceptor
 * 可以替换host
 */
class HostInterceptor: Interceptor {

    companion object {
        const val NEW_HOST_HEADER_NAME = "myHost"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        return if (oldRequest.headers[NEW_HOST_HEADER_NAME] != null) {
            val host = oldRequest.headers[NEW_HOST_HEADER_NAME]!!
            val builder = Request.Builder()
                .method(oldRequest.method, oldRequest.body)
                .cacheControl(oldRequest.cacheControl)
                .headers(oldRequest.headers)
                .removeHeader(NEW_HOST_HEADER_NAME)
            val newRequest = if (oldRequest.url.host != host) {
                DSLog.net().info("old host is ${oldRequest.url.host}, new host is $host.")
                builder.url(replaceHost(host, oldRequest.url)).build()
            } else {
                builder.url(oldRequest.url) .build()
            }
            chain.proceed(newRequest)
        } else {
            chain.proceed(oldRequest)
        }
    }

    private fun replaceHost(host: String, oldUrl: HttpUrl) : String {
        var newUrl = "${
            if (oldUrl.isHttps) "https" else "http"
        }://$host${oldUrl.encodedPath}"
        var isFirst = true
        oldUrl.queryParameterNames.forEach {
            val param = oldUrl.queryParameter(it)
            if (param != null) {
                if (isFirst) {
                    isFirst = false
                    newUrl += "?$it$param"
                } else {
                    newUrl += "&$it$param"
                }
            }
        }
        return newUrl
    }
}