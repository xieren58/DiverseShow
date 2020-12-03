package com.four.ds_home.main

import android.os.Bundle
import com.four.api_weather.WeatherLauncher
import com.four.base.zy.activity.BaseActivity
import com.four.ds_home.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnToWeather.setOnClickListener {
            WeatherLauncher.openWeatherPage(this)
        }
    }
}