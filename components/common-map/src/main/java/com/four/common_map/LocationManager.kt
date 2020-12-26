package com.four.common_map

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.four.common_util.log.DSLog
import java.lang.Exception
import java.util.concurrent.Executors

class LocationManager(private val context: Context,
                      private val exactLocationLiveData: MutableLiveData<AMapLocation?>,
                      private val normaLocationLiveData: MutableLiveData<AMapLocation?>
    ) : AMapLocationListener {

    private val mapExecutor = Executors.newSingleThreadExecutor()

    private var isFirstOfTwice = true

    private var needExactLocation = false
    private var needNormalLocation = false

    private val client: AMapLocationClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DSLog.map().info("LocationManager init.")

        val option = AMapLocationClientOption()
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        option.httpTimeOut = 10000
        option.isNeedAddress = true
        option.isWifiScan = true
        option.isOnceLocation = true //只请求一次
        option.isGpsFirst = true

        AMapLocationClient(context).apply {
            setLocationOption(option)
            setLocationListener(this@LocationManager)
        }
    }

    private var lastLocation: AMapLocation? = null

    override fun onLocationChanged(location: AMapLocation?) {
        DSLog.map().debug("location change ${location?.city}")
        if (mapExecutor.isShutdown) {
            DSLog.map().error("location executor failed!!!")
            Toast.makeText(context, "map线程崩溃,请联系zzy", Toast.LENGTH_SHORT).show()
            return
        }

        mapExecutor.submit {
            if (needExactLocation) {
                if (isFirstOfTwice) {
                    isFirstOfTwice = false
                    client.startLocation()
                } else {
                    isFirstOfTwice = true
                }
            }

            //回掉
            if (location != null) {
                lastLocation = location
            }
            if (needNormalLocation) {
                needNormalLocation = false
                normaLocationLiveData.postValue(location)
            }
            if (needExactLocation && isFirstOfTwice) {
                needExactLocation = false
                exactLocationLiveData.postValue(location)
                DSLog.map().debug("exact location changed.")
            }
        }
    }

    /**
     * 请求一次精确位置，由于会触发两次定位，所以比较耗时
     */
    fun requestExactLocation() {
        mapExecutor.submit {
            if (!needExactLocation) {
                needExactLocation = true
                if (!needNormalLocation) {
                    client.startLocation()
                }
            }
        }
    }

    /**
     * 请求一次普通位置
     */
    fun requestNormalLocation() {
        mapExecutor.submit {
            if (!needNormalLocation) {
                needNormalLocation = true
                if (!needExactLocation) {
                    client.startLocation()
                }
            }
        }
    }
}