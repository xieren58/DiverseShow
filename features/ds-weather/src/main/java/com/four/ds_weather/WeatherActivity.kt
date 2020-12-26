package com.four.ds_weather

import android.os.Bundle
import com.four.base.zy.activity.BaseControllerActivity

class WeatherActivity : BaseControllerActivity<WeatherController>() {

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = false

    override fun createController(): WeatherController = WeatherController(this, this)
}

