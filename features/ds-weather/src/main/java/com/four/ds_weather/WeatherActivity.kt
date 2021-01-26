package com.four.ds_weather

import com.four.base.zy.activity.BaseControllerActivity

class WeatherActivity : BaseControllerActivity<WeatherController>() {

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = true

    override fun createController(): WeatherController = WeatherController(this, this)
}

