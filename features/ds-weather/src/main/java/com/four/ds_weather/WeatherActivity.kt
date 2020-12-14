package com.four.ds_weather

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.four.base.zy.activity.BaseModelActivity
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : BaseModelActivity<WeatherModel>() {


    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = false

    override fun bindView() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val viewModel = viewModel!!
        viewModel.weatherLiveData.observe(this) {
            text.text = "${it.city} ${it.data[0].wea} ${it.data[0].hours[4].tem}°C"
        }
        btnRequest.setOnClickListener {
            viewModel.requestWeekWeather(lifecycle)
        }
    }
}


//https://weatherweek.api.bdymkt.com/week
/*
city	STRING	非必须	城市名称, 不要带市和区; 如: 青岛、铁西
cityid	STRING	非必须	城市编号
ip	STRING	非必须	IP地址
province	STRING	非必须	所在省, 防止city重名用 如: 山东
* */
//f6edd8b075d3478cb403decd9ce3a6b5
/*

X-Bce-Signature: AppCode/6f4ac66971454093bcceb34521bb541e

https://weather.api.bdymkt.com/day
cityid	STRING	非必须	城市ID
city	STRING	非必须	城市名称, 不要带市和区; 如: 青岛、铁西
province	STRING	非必须	省份名称, 不要带省和自治区
ip	STRING	非必须	IP地址
f6edd8b075d3478cb403decd9ce3a6b5
* */


