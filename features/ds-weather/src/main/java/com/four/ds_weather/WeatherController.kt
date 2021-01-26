package com.four.ds_weather

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.amap.api.location.AMapLocation
import com.four.base.zy.BaseController
import com.four.base.zy.IViewFinder
import com.four.common_map.LocationHelper
import com.four.common_util.permission.PermissionHelper
import com.four.ds_weather.controllers.MainWeatherContentController
import com.four.ds_weather.controllers.TopBarController
import com.four.ds_weather.net.DayWeatherBean
import com.four.ds_weather.net.WeekWeatherBean

class WeatherController(context: Context, val activity: FragmentActivity)
    : BaseController<WeatherModel>(context), IWeatherCallback, ILocationCallback {

    private var location = AMapLocation("")

    init {
        controllers.add(TopBarController(context))
        controllers.add(MainWeatherContentController(context))
    }

    override fun bindView(finder: IViewFinder) { }


    override fun initData() {
        viewModel?.also {
            it.dayLiveData.observe(lifecycleOwner) { bean ->
                onDayWeatherData(bean)
            }
            it.weekLiveData.observe(lifecycleOwner) { bean ->
                onWeekWeatherData(bean)
            }
        }

        checkPermission{
            requestLocation {
                onLocationChanged(location)
                requestWeatherData()
            }
        }
    }


    override fun onLocationChanged(location: AMapLocation) {
        controllers.forEach { controller ->
            if (controller is ILocationCallback) {
                controller.onLocationChanged(location)
            }
        }
    }


    override fun onDayWeatherData(bean: DayWeatherBean) {
        controllers.forEach { controller ->
            if (controller is IWeatherCallback) {
                controller.onDayWeatherData(bean)
            }
        }
    }

    override fun onWeekWeatherData(bean: WeekWeatherBean) {
        controllers.forEach { controller ->
            if (controller is IWeatherCallback) {
                controller.onWeekWeatherData(bean)
            }
        }
    }

    private fun checkPermission(onGranted: () -> Unit) {
        PermissionHelper.simpleRequestPermission(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            object : PermissionHelper.Callback {
                override fun onGranted() {
                    onGranted.invoke()
                }

                override fun onDenied(deniedPermissions: List<String>) { }
            },
            forceRequest = false,
            object : PermissionHelper.OnShouldShowToastListener {
                override fun onShowToast() {
                    Toast.makeText(activity, "没有定位权限", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun requestLocation(onChanged: () -> Unit) {
        LocationHelper.normalLocationLiveData.observe(lifecycleOwner) {
            if (it != null) {
                if (it.district != null && location.district != it.district) {
                    location = it
                    onChanged.invoke()
                } else if (it.city != null && it.city != location.city) {
                    location = it
                    onChanged.invoke()
                }
            }
        }
        LocationHelper.requestNormalLocation()
    }

    private fun requestWeatherData() {
        viewModel?.also {
            it.requestDayWeather(lifecycleOwner.lifecycle, location.district ?: location.city)
            it.requestWeekWeather(lifecycleOwner.lifecycle, location.district ?: location.city)
        }
    }
}