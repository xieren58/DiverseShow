package com.four.common_net.interceptors

import com.four.common_util.log.DSLog
import com.four.common_util.log.LogLevel
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import java.lang.Exception
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * 日志打印器，建议放在net拦截器里面
 */
class HttpLoggingInterceptor : Interceptor {

    companion object {

        //4kb
        const val BODY_READER_MAX = 4 * 1024
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!DSLog.net().openLog || DSLog.net().logLevel == LogLevel.NONE) {
            return chain.proceed(chain.request())
        }

        //有缓存的连接时，会直接复用
        val conn = chain.connection()
        val request = chain.request()
        val requestBody = request.body
        DSLog.net().debug("---> Client, ${conn?.protocol() ?: ""}${request.method} ${request.url}")
        request.headers.forEach {
            DSLog.net().debug("${it.first}: ${it.second}")
        }
        requestBody?.let {
            if (it.contentType() != null && null == request.headers["Content-Type"]) {
                DSLog.net().debug("Content-Type: ${it.contentType()}")
            }
            if (it.contentLength() > 0 && null == request.headers["Content-Length"]) {
                DSLog.net().debug("Content-Length: ${it.contentLength()}")
            }
            //http 2.0 双工，支持服务端推送
            if (requestBody.isDuplex()) {
                DSLog.net().debug("---> End, the http is duplex.")
            }
            //只能被write一次，没必要再去读
            if (requestBody.isOneShot()) {
                DSLog.net().debug("---> End, the body is one-off.")
                return@let
            }
            if (it.contentLength() > BODY_READER_MAX) {
                DSLog.net().debug("---> End, cannot read more than 4 * 1024 byte, body size = ${it.contentLength()}.")
                return@let
            }
            val buffer = Buffer()
            it.writeTo(buffer)
            val charset = it.contentType()?.charset() ?: Charset.defaultCharset()
            DSLog.net().debug(buffer.readString(charset))
            DSLog.net().debug("---> End, ${it.contentLength()}-byte body.")
        }
        if (requestBody == null) {
            DSLog.net().debug("---> End, no body.")
        }

        //事件交给下一个拦截器
        val response: Response
        val curTimeMillis = System.currentTimeMillis()
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            DSLog.net().info("<--- Http failed: spent ${System.currentTimeMillis() - curTimeMillis} ms, ${e.message}.")
            throw e
        }
        val duration = System.currentTimeMillis() - curTimeMillis

        DSLog.net().debug("---> Server, ${response.protocol} ${response.code} ${response.request.url}")
        response.headers.forEach {
            DSLog.net().debug("${it.first}: ${it.second}")
        }
        val body = response.body
        if (body == null || !response.promisesBody()) {
            DSLog.net().debug("----> End, spent ${duration}ms.")
        } else {
            if (body.contentLength() > BODY_READER_MAX) {
                DSLog.net().debug("---> End, spent ${duration}ms, cannot read more than 4 * 1024 byte, body size = ${body.contentLength()}.")
            } else {
                body.source().request(Long.MAX_VALUE)
                var buffer = body.source().buffer
                val charset = body.contentType()?.charset() ?: Charset.defaultCharset()
                var gzippedLength: Long? = null
                if ("gzip".equals(response.headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }
                //只能读一次
                DSLog.net().debug(buffer.clone().readString(charset))
                if (gzippedLength != null) {
                    DSLog.net().debug("<--- End, spent ${duration}ms, ${buffer.size}-byte, ${gzippedLength}-gzipped-byte.")
                } else {
                    DSLog.net().debug("<--- End, spent ${duration}ms, ${buffer.size}-byte.")
                }
            }
        }

        return response
    }
}