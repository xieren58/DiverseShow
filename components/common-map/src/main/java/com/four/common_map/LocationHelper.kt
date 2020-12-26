package com.four.common_map

import androidx.lifecycle.*
import com.amap.api.location.AMapLocation
import com.ds.global.DSApplication
import com.four.common_util.log.DSLog

object LocationHelper {

    //为空则请求失败
    val exactLocationLiveData = MutableLiveData<AMapLocation?>()

    //为空则请求失败
    val normalLocationLiveData = MutableLiveData<AMapLocation?>()

    private val locationManager = LocationManager(DSApplication.instance, exactLocationLiveData, normalLocationLiveData)

    fun requestExactLocation(lifecycleOwner: LifecycleOwner, observer: Observer<AMapLocation?>) {
        DSLog.map().debug("observe exact location, $lifecycleOwner")
        exactLocationLiveData.observe(lifecycleOwner, observer)
        locationManager.requestExactLocation()
    }

    fun requestNormalLocation(lifecycleOwner: LifecycleOwner, observer: Observer<AMapLocation?>) {
        DSLog.map().debug("observe normal location,  $lifecycleOwner")
        normalLocationLiveData.observe(lifecycleOwner, observer)
        locationManager.requestNormalLocation()
    }
}