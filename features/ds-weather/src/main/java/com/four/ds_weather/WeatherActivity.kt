package com.four.ds_weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.four.base.zy.BaseController
import com.four.base.zy.activity.BaseControllerActivity
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : BaseControllerActivity<BaseController<*>>() {

    override fun createController(): BaseController<*>? = null

    override fun getLayoutId(): Int = R.layout.activity_weather

    override fun hideActionBar(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val a = createAnimOfShow(abcd)
        ass!!.setOnClickListener {
            abcd.visibility = View.VISIBLE
            abcd.startAnimation(a)
        }

        val intent = Intent()
    }
}

private fun createAnimOfShow(view: View): Animation? {
    //panel 动画300ms

    //panel 动画300ms
    val animation: Animation = AlphaAnimation(0f, 1f)
    animation.duration = 300
    animation.startOffset = 2000
    return animation

   // return defaultAnim
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

X-Bce-Signature: AppCode/6f4ac66971454093bcceb34521bb541e

https://weather.api.bdymkt.com/day
cityid	STRING	非必须	城市ID
city	STRING	非必须	城市名称, 不要带市和区; 如: 青岛、铁西
province	STRING	非必须	省份名称, 不要带省和自治区
ip	STRING	非必须	IP地址
f6edd8b075d3478cb403decd9ce3a6b5
* */


