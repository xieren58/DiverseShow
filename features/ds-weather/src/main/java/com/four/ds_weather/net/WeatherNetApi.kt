package com.four.ds_weather.net

import io.reactivex.Observable
import retrofit2.http.*

interface WeatherNetApi {

    /**
     * 其实两个域名可以用一样的...
     */


    @GET("week")
    @Headers(
        "Accept-Charset: UTF-8",
        "Accept: application/json",
        "X-Bce-Signature: AppCode/f6edd8b075d3478cb403decd9ce3a6b5",
        "myHost: weatherweek.api.bdymkt.com"
    )
    fun getWeekWeather(@Query("city") city: String) : Observable<WeekWeatherBean?>

    @GET("day")
    @Headers(
        "Accept-Charset: UTF-8",
        "Accept: application/json",
        "X-Bce-Signature: AppCode/f6edd8b075d3478cb403decd9ce3a6b5",
        "myHost: weather.api.bdymkt.com"
    )
    fun getDayWeather(@Query("city") city: String) : Observable<DayWeatherBean>
}