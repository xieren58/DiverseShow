package com.four.ds_weather

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.four.common_net.util.RxUtil
import com.four.common_util.log.DSLog
import com.four.ds_weather.net.WeatherHelper
import com.four.ds_weather.net.WeekWeatherBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class WeatherModel : ViewModel() {

    fun requestWeekWeather(lifecycle: Lifecycle) {
        WeatherHelper.client
            .getWeekWeather("大足")
            .compose(RxUtil.ioToUiAndAutoDispose(lifecycle))
            .subscribe(object : Observer<WeekWeatherBean?> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: WeekWeatherBean) {
                    DSLog.d(t.city + t.data[0].date)
                }

                override fun onError(e: Throwable) {
                    DSLog.d(e.message!!)
                }

                override fun onComplete() {
                }
            } )
    }
}