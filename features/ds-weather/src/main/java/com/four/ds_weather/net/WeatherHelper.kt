package com.four.ds_weather.net

import com.four.common_net.util.ClientUtil

object WeatherHelper {

    const val WEEK_BASE_URL = "https://weatherweek.api.bdymkt.com"

    val client = ClientUtil
        .createNewClientBuilder(WEEK_BASE_URL)
        .build()
        .create(WeatherNetApi::class.java)
}