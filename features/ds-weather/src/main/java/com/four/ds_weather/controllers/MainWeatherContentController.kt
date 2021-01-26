package com.four.ds_weather.controllers

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.ds_weather.IWeatherCallback
import com.four.ds_weather.R
import com.four.ds_weather.WeatherModel
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

class MainWeatherContentController(context: Context) : BaseController<WeatherModel>(context), IWeatherCallback {

    private lateinit var tvTemperature: TextView
    private lateinit var tvSky: TextView
    private lateinit var tvAir: TextView
    private lateinit var tvWater: TextView
    private lateinit var tvMinus: TextView

    override fun bindView(finder: IViewFinder) {
        tvTemperature = finder.findViewNoNull(R.id.tvTodayTemperature)
        tvAir = finder.findViewNoNull(R.id.tvTodayAir)
        tvSky = finder.findViewNoNull(R.id.tvTodaySky)
        tvWater = finder.findViewNoNull(R.id.tvTodayWater)
        tvMinus = finder.findViewNoNull(R.id.tvMinus)
    }

    @SuppressLint("SetTextI18n")
    override fun onDayWeatherData(bean: DayWeatherBean) {
        tvTemperature.text = bean.tem.replace("-", "")
        if(bean.tem.contains("-")) {
            tvMinus.visibility = View.VISIBLE
        } else {
            tvMinus.visibility = View.GONE
        }
        tvSky.text = bean.wea
        tvAir.text = "空气${bean.air_level} ${bean.air}"
        //md 居然不下发降水概率
        tvWater.text = "降水概率 ${getWaterProbability(bean.wea)}"
    }

    override fun onWeekWeatherData(bean: WeekWeatherBean) {
    }

    private fun getWaterProbability(wea: String) : String {
        return when {
            wea.contains("雨") -> {
                "90%"
            }
            wea.contains("雪") -> {
                "20%"
            }
            else -> {
                when (wea) {
                    "阴" -> "40%"
                    "多云" -> "30%"
                    else -> "0%"
                }
            }
        }
    }

}