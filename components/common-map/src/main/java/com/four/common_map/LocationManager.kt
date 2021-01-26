package com.four.common_map

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.four.common_util.log.DSLog

class LocationManager(private val context: Context,
                      private val exactLocationLiveData: MutableLiveData<AMapLocation?>,
                      private val normaLocationLiveData: MutableLiveData<AMapLocation?>
    ) : AMapLocationListener {

    private val client: AMapLocationClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DSLog.map().info("LocationManager init.")
        AMapLocationClient(context).apply {
            setLocationListener(this@LocationManager)
        }
    }

    private var lastLocation: AMapLocation? = null

    @Volatile private var isLocating = false

    override fun onLocationChanged(location: AMapLocation?) {
        DSLog.map().debug("location change ${location?.address}")
        if (location != null) {
            lastLocation = location
        }
        normaLocationLiveData.postValue(location)
        exactLocationLiveData.postValue(location)
        isLocating = false
    }

    /**
     * 请求一次精确位置，必须拿到新的定位才可以
     */
    fun requestExactLocation() {
        tryStartLocation()
    }

    /**
     * 请求一次普通位置，可以复用之前的就请求好的位置
     */
    fun requestNormalLocation() {
        tryStartLocation()
        if (lastLocation != null) {
            normaLocationLiveData.postValue(lastLocation)
        }
    }

    private fun tryStartLocation() {
        if (!isLocating) {
            isLocating = true
            //提高首次定位速度
            if (lastLocation == null || lastLocation?.address.isNullOrEmpty()) {
                client.setLocationOption(simpleLocationOption)
                DSLog.map().debug("simple locating.")
            } else {
                client.setLocationOption(exactLocationOption)
            }
            client.startLocation()
        }
    }

    private val simpleLocationOption = AMapLocationClientOption().apply {
        locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        httpTimeOut = 15000
        isGpsFirst = false
        isNeedAddress = true
        isOnceLocation = true
    }

    private val exactLocationOption = AMapLocationClientOption().apply {
        locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        httpTimeOut = 20000
        isNeedAddress = true
        isOnceLocation = true
        isGpsFirst = true
        isWifiScan = true
    }

}