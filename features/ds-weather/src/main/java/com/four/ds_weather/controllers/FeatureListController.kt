package com.four.ds_weather.controllers

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.ds_weather.IWeatherCallback
import com.four.ds_weather.R
import com.four.ds_weather.WeatherModel
import com.four.ds_weather.controllers.adapter.FeatureListAdapter
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

class FeatureListController(context: Context) : BaseController<WeatherModel>(context), IWeatherCallback {

    private lateinit var rvFeature: RecyclerView
    private lateinit var tv7Day: TextView

    private lateinit var adapter: FeatureListAdapter

    override fun bindView(finder: IViewFinder) {
        rvFeature = finder.findViewNoNull(R.id.rvFeatureList)
        tv7Day = finder.findViewNoNull(R.id.tvWatchFeatureDay)
    }

    override fun initData() {
        adapter = FeatureListAdapter()
        rvFeature.layoutManager = LinearLayoutManager(context)
        rvFeature.adapter = adapter
        val notValidBean = FeatureListAdapter.DataWrapper(null, false)
        adapter.setData(listOf(notValidBean, notValidBean, notValidBean))
    }

    override fun onDayWeatherData(bean: DayWeatherBean) {
    }

    override fun onWeekWeatherData(bean: WeekWeatherBean) {
        val list = mutableListOf<FeatureListAdapter.DataWrapper>()
        var count = 0
        bean.data.forEach out@ {
            if (count > 2) {
                return@out
            }
            count++
            val wrapper = FeatureListAdapter.DataWrapper(it, true)
            list.add(wrapper)
        }
        adapter.setData(list)
    }
}