package com.four.ds_weather

import android.content.Context
import android.widget.Button
import android.widget.TextView
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder

class WeatherController(context: Context): BaseController<WeatherModel>(context) {

    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var btnPull: Button

    override fun bindView(finder: IViewFinder) {
        tv1 = finder.findViewNoNull(R.id.text1)
        tv2 = finder.findViewNoNull(R.id.text2)
        btnPull = finder.findViewNoNull(R.id.btnRequest)
        btnPull.setOnClickListener{ requestData() }
    }

    private fun requestData() {
        viewModel?.apply {
            weekLiveData.observe(lifecycleOwner) {
                tv1.text = "${it.city} ${it.data[0].hours[0].wea}"
            }
            dayLiveData.observe(lifecycleOwner) {
                tv2.text = "${it.city} ${it.air}"
            }

            requestDayWeather(lifecycleOwner.lifecycle)
            requestWeekWeather(lifecycleOwner.lifecycle)
        }
    }
}