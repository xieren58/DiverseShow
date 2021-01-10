package com.four.ds_home.main

import android.os.Bundle
import android.widget.Toast
import com.ds.global.IFirstFramePage
import com.ds.hotfix.FixTest
import com.example.opencv.OpenCVLauncher
import com.four.api_weather.WeatherLauncher
import com.four.base.zy.activity.BaseActivity
import com.four.ds_home.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), IFirstFramePage {

    private val hotfixLauncher by lazy {
         FixTest()
    }

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnToWeather.setOnClickListener {
            WeatherLauncher.openWeatherPage(this)
        }

        btnToHotFix.setOnClickListener {
            hotfixLauncher.launchFixTest(this)
        }

        btnToOpenCV.setOnClickListener {
            Toast.makeText(this,OpenCVLauncher.launchOpenCV(this),Toast.LENGTH_SHORT).show()
        }
    }
}