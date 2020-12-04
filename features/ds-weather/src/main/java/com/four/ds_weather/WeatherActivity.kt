package com.four.ds_weather

import com.four.base.zy.BaseController
import com.four.base.zy.activity.BaseControllerActivity

class WeatherActivity : BaseControllerActivity<BaseController<*>>() {

    override fun createController(): BaseController<*>? = null

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = false
}

//https://weatherweek.api.bdymkt.com/week
/*
city	STRING	非必须	城市名称, 不要带市和区; 如: 青岛、铁西
cityid	STRING	非必须	城市编号
ip	STRING	非必须	IP地址
province	STRING	非必须	所在省, 防止city重名用 如: 山东
* */
//f6edd8b075d3478cb403decd9ce3a6b5
/*
GET /test HTTP/1.1
Host: test.api.bdymkt.com
X-Bce-Signature: AppCode/6f4ac66971454093bcceb34521bb541e
* */
