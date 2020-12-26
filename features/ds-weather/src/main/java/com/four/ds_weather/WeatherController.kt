package com.four.ds_weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.common_map.LocationHelper
import com.four.common_util.log.DSLog
import com.four.common_util.permission.PermissionHelper

class WeatherController(context: Context, val activity: FragmentActivity): BaseController<WeatherModel>(context) {

    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    private lateinit var btnPull: Button

    override fun bindView(finder: IViewFinder) {
        tv1 = finder.findViewNoNull(R.id.text1)
        tv2 = finder.findViewNoNull(R.id.text2)
        btnPull = finder.findViewNoNull(R.id.btnRequest)
        btnPull.setOnClickListener{ requestData() }
    }

    override fun initData() {
        PermissionHelper.simpleRequestPermission(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            object : PermissionHelper.Callback {
                override fun onGranted() {
                    LocationHelper.requestExactLocation(lifecycleOwner) {
                        DSLog.d("get location ${it?.city}")
                    }
                }

                override fun onDenied(deniedPermissions: List<String>) {
                }

            })
    }

    @SuppressLint("SetTextI18n")
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