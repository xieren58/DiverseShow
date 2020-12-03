package com.four.base.zy

import android.view.View
import androidx.annotation.IdRes

interface IViewFinder {
    fun <V : View> findView(@IdRes id: Int) : V?
}