package com.four.common_net.interceptors

import com.four.common_util.log.DSLog
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.nio.charset.StandardCharsets

/**
 * 日志打印器，建议放在net拦截器里面
 */
class HttpLogInterceptor : Interceptor {

    companion object {

        //4kb
        const val BODY_READER_MAX = 4 * 1024
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!DSLog.net().openLog) {
            return chain.proceed(chain.request())
        }

        val space = "  "

        val request = chain.request()
        val logBuilder = StringBuilder()

        logBuilder.append("\n------> net intercept, client\n")

        logBuilder.append(request.method).append(space).append(request.url).append("\n")
        request.headers.forEach {
            logBuilder.append(it.first).append(": ").append(it.second).append("\n")
        }

        var bodyStr: String? = null
        request.body?.let {
            logBuilder.append(it.contentType()).append(space).append(it.contentLength()).append("\n")
            if (it.contentLength() > BODY_READER_MAX) {
                logBuilder.append("cannot read more than 4 * 1024 byte.")
            } else {
                val buffer = Buffer()
                it.writeTo(buffer)
                val byteArray = buffer.readByteArray()
                //因为下面toRequestBody默认是utf-8
                bodyStr = String(byteArray, StandardCharsets.UTF_8)
                logBuilder.append(bodyStr).append("\n")
            }
        }

        logBuilder.append("<------ net intercept end, client\n")
        DSLog.net().debug(logBuilder.toString())

        val newReq = Request.Builder()
            .cacheControl(request.cacheControl)
            .headers(request.headers)
            .tag(request.tag())
            .url(request.url)

        newReq.method(request.method, bodyStr?.toRequestBody(request.body?.contentType()) ?: request.body)

        //事件交给下一个拦截器
        val response = chain.proceed(newReq.build())

        return response
//        logBuilder.clear()
//        logBuilder.append("\n------> net intercept, server\n")
//
//        logBuilder.append(response.protocol).append(space).append(response.message).append(space)
//            .append(response.code).append(space).append(response.request.url).append("\n")
//        response.headers.forEach {
//            logBuilder.append(it.first).append(": ").append(it.second).append("\n")
//        }
//
//        var bodyBytes: ByteArray? = null
//        if (response.isSuccessful && response.body != null) {
//            response.body?.let {
//                logBuilder.append(it.contentType()).append(space).append(it.contentLength()).append("\n")
//                if (it.contentLength() > BODY_READER_MAX) {
//                    logBuilder.append("cannot read more than 4 * 1024 byte.")
//                } else {
//                    //因为下面toRequestBody默认是utf-8
//                    bodyBytes = it.bytes()
//                    bodyStr = String(bodyBytes!!, StandardCharsets.UTF_8)
//                    logBuilder.append(bodyStr).append("\n")
//                }
//            }
//        }
//
//        logBuilder.append("<------ net intercept end, server\n")
//        DSLog.net().debug(logBuilder.toString())
//
//        return Response.Builder()
//            .code(response.code)
//            .headers(response.headers)
//            .body(bodyBytes?.toResponseBody(response.body?.contentType()))
//            .cacheResponse(response.cacheResponse)
//            .message(response.message)
//            .handshake(response.handshake)
//            .networkResponse(response.networkResponse)
//            .cacheResponse(response.cacheResponse)
//            .priorResponse(response.priorResponse)
//            .sentRequestAtMillis(response.sentRequestAtMillis)
//            .build()
    }
}