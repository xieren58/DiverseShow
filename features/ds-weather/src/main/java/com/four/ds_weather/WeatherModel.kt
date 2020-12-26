package com.four.ds_weather

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.four.common_net.util.ioToUiAndAutoDispose
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeatherHelper
import com.four.ds_weather.net.WeekWeatherBean

class WeatherModel : ViewModel() {

    val weekLiveData = MutableLiveData<WeekWeatherBean>()

    val dayLiveData = MutableLiveData<DayWeatherBean>()

    fun requestWeekWeather(lifecycle: Lifecycle) {
        WeatherHelper.client
            .getWeekWeather("北京")
            .ioToUiAndAutoDispose(lifecycle, {
            weekLiveData.value = it
        })
    }

    fun requestDayWeather(lifecycle: Lifecycle) {
        WeatherHelper.client
            .getDayWeather("北京")
            .ioToUiAndAutoDispose(lifecycle, {
            dayLiveData.value = it
        })
    }
}