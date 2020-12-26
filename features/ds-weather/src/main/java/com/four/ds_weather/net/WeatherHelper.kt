package com.four.ds_weather.net

import com.four.common_net.interceptors.HostInterceptor
import com.four.common_net.interceptors.HttpLoggingInterceptor
import com.four.common_net.interceptors.InterceptorFactory
import com.four.common_net.util.ClientUtil
import okhttp3.OkHttpClient

object WeatherHelper {

    const val WEEK_BASE_URL = "https://weatherweek.api.bdymkt.com"

    val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(InterceptorFactory.createHttpLogInterceptor())
        .addInterceptor(InterceptorFactory.createHostInterceptor())
        .build()

    val client = ClientUtil
        .createNewClientBuilder(WEEK_BASE_URL, okHttpClient)
        .build()
        .create(WeatherNetApi::class.java)
}