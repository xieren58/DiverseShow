package com.four.ds_weather

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.four.common_net.util.RxUtil
import com.four.common_util.log.DSLog
import com.four.ds_weather.net.WeatherHelper
import com.four.ds_weather.net.WeekWeatherBean

class WeatherModel : ViewModel() {

    val weatherLiveData = MutableLiveData<WeekWeatherBean>()

    fun requestWeekWeather(lifecycle: Lifecycle) {
        RxUtil.dataToUISafely(WeatherHelper.client.getWeekWeather("大足"), lifecycle, {
            weatherLiveData.value = it
        })
    }
}