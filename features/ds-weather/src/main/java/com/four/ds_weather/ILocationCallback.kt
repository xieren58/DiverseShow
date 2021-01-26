package com.four.ds_weather

import com.amap.api.location.AMapLocation

interface ILocationCallback {
    fun onLocationChanged(location: AMapLocation)
}