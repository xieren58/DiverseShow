package com.four.ds_weather

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.four.common_net.util.RxUtil
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeatherHelper
import com.four.ds_weather.net.WeekWeatherBean

class WeatherModel : ViewModel() {

    val weekLiveData = MutableLiveData<WeekWeatherBean>()

    val dayLiveData = MutableLiveData<DayWeatherBean>()

    fun requestWeekWeather(lifecycle: Lifecycle) {
        RxUtil.dataToUISafely(WeatherHelper.client.getWeekWeather("大足"), lifecycle, {
            weekLiveData.value = it
        })
    }

    fun requestDayWeather(lifecycle: Lifecycle) {
        RxUtil.dataToUISafely(WeatherHelper.client.getDayWeather("大足"), lifecycle, {
            dayLiveData.value = it
        })
    }}