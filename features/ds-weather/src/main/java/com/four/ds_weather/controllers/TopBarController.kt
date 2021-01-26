package com.four.ds_weather.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.ds_weather.ILocationCallback
import com.four.ds_weather.R
import com.four.ds_weather.WeatherModel

class TopBarController(context: Context) : BaseController<WeatherModel>(context), ILocationCallback {

    private lateinit var tvTitle: TextView

    override fun bindView(finder: IViewFinder) {
        tvTitle = finder.findViewNoNull(R.id.tvLocationTitle)
    }

    @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: AMapLocation) {
        tvTitle.text = "${
            location.district ?: location.city
        }${
            if (!location.street.isNullOrEmpty()) " ${location.street}" else "" 
        }"
    }
}