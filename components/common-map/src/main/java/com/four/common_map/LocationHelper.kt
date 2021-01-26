package com.four.common_map

import android.app.Application
import androidx.lifecycle.*
import com.amap.api.location.AMapLocation
import com.ds.global.DSApplication
import com.four.app_init_handler.api.AppLifeEvent
import com.four.app_init_handler.api.AppLifeEventInt
import com.four.app_init_handler.api.OnAppLifeChanged
import com.four.common_util.log.DSLog
import io.reactivex.schedulers.Schedulers

object LocationHelper {

    //为空则请求失败
    val exactLocationLiveData = MutableLiveData<AMapLocation?>()

    //为空则请求失败
    val normalLocationLiveData = MutableLiveData<AMapLocation?>()

    private var locationManager: LocationManager? = null

    fun requestExactLocation() {
        DSLog.map().debug("request exact location")
        locationManager?.requestExactLocation()
    }

    fun requestNormalLocation() {
        DSLog.map().debug("request normal location")
        locationManager?.requestNormalLocation()
    }

    //初始化定位
    @OnAppLifeChanged(AppLifeEvent.AFTER_FIRST_FRAME)
    fun init(app: Application) {
        Schedulers.computation().scheduleDirect {
            locationManager = LocationManager(app, exactLocationLiveData, normalLocationLiveData)
            requestNormalLocation()
        }
    }
}