package com.four.common_net.util

import com.four.common_net.interceptors.InterceptorFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ClientUtil {

    //连接超时，缩减到5s
    const val CONNECT_TIME_OUT = 5L

    //允许大文件读写，延长到20s
    const val READ_TIME_OUT = 20L
    const val WRITE_TIME_OUT = 20L

    fun createNewOkClientBuilder() = OkHttpClient.Builder()
        .addNetworkInterceptor(InterceptorFactory.createHttpLogInterceptor())
        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)

    fun createNewClientBuilder(baseUrl: String,
                               client: OkHttpClient = createNewOkClientBuilder().build()
    ) = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

}