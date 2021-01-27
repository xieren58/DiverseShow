package com.four.ds_weather.controllers

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.ds_weather.IWeatherCallback
import com.four.ds_weather.R
import com.four.ds_weather.WeatherModel
import com.four.ds_weather.controllers.adapter.Feature24HourListAdapter
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

class Feature24HourController(context: Context) : BaseController<WeatherModel>(context), IWeatherCallback {

    private lateinit var rv24Hour: RecyclerView

    private var adapter = Feature24HourListAdapter()

    override fun bindView(finder: IViewFinder) {
        rv24Hour = finder.findViewNoNull(R.id.rv24Weather)
    }

    override fun initData() {
        rv24Hour.adapter = adapter
        rv24Hour.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    override fun onDayWeatherData(bean: DayWeatherBean) {
        val data = mutableListOf<Feature24HourListAdapter.Bean>()
        var isFirst = true
        bean.hours.forEach {
            if (isFirst) {
                isFirst = false
                return@forEach
            }
            data.add(Feature24HourListAdapter.Bean(it.hours, "${it.tem}°", it.wea, "${it.win} ${it.win_speed}", it.aqi))
        }
        adapter.setData(data)
    }

    override fun onWeekWeatherData(bean: WeekWeatherBean) { }
}