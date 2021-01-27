package com.four.ds_weather

import androidx.annotation.UiThread
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

interface IWeatherCallback {

    @UiThread
    fun onDayWeatherData(bean: DayWeatherBean)

    @UiThread
    fun onWeekWeatherData(bean: WeekWeatherBean)
}