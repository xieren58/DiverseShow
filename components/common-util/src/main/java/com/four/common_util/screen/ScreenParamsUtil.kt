package com.four.common_util.screen

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

class ScreenParamsUtil {

    fun getScreenDensity(context: Context) : Float {
        return if (context is Activity) {
            val metrics = DisplayMetrics()
            context.display!!.getRealMetrics(metrics)
            metrics.density
        } else {
            context.resources.displayMetrics.density
        }
    }

    fun getScreenWidthAndHeight(context: Context) : Pair<Int, Int> {
        return if (context is Activity) {
            val metrics = DisplayMetrics()
            context.display!!.getRealMetrics(metrics)
            Pair(metrics.widthPixels, metrics.heightPixels)
        } else {
            val metrics = context.resources.displayMetrics
            Pair(metrics.widthPixels, metrics.heightPixels)
        }
    }
}