package com.four.api_weather

import android.app.Application
import android.content.Context
import android.content.Intent
import com.four.ds_weather.WeatherActivity

object WeatherLauncher {

    fun openWeatherPage(context: Context) {
        val intent = Intent(context, WeatherActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}