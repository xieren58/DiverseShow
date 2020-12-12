package com.four.ds_weather.net

import io.reactivex.Observable
import retrofit2.http.*

interface WeatherNetApi {

    @GET(value = "/week")
    @Headers(
        "Accept-Charset: UTF-8",
        "Accept: application/json",
        "X-Bce-Signature: AppCode/f6edd8b075d3478cb403decd9ce3a6b5",
    )
    fun getWeekWeather(@Query("city") city: String) : Observable<WeekWeatherBean?>
}