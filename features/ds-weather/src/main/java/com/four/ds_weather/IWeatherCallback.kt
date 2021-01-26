package com.four.ds_weather

import com.amap.api.location.AMapLocation
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

interface IWeatherCallback {

    fun onDayWeatherData(bean: DayWeatherBean)

    fun onWeekWeatherData(bean: WeekWeatherBean)
}