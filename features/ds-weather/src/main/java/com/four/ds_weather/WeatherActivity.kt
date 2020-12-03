package com.four.ds_weather

import com.four.base.zy.BaseController
import com.four.base.zy.activity.BaseControllerActivity

class WeatherActivity : BaseControllerActivity<BaseController<*>>() {

    override fun createController(): BaseController<*>? = null

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = false
}