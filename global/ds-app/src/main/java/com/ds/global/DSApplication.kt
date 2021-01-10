package com.ds.global

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.four.app_init_handler.AppInitInjector
import com.four.app_init_handler.StringConstant
import com.four.app_init_handler.api.IAppLife

class DSApplication : Application() {

    companion object {

        lateinit var instance: DSApplication
            private set

        private const val TAG = "ds-application"
    }

    private val appLifeListener: IAppLife = AppInitInjector

    private var firstFrameShowed = false
    private val handler = Handler(Looper.getMainLooper())

    private val activityCallback = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            tryRegisterFirstFrameListener(activity)
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }
    }

    init {
        instance = this
        AppInitInjector.init()
    }

    override fun attachBaseContext(base: Context) {
        appLifeListener.beforeAttachContext(base)
        super.attachBaseContext(base)
        appLifeListener.attachContext(instance)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityCallback)
        appLifeListener.onCreate(instance)
    }

    private fun tryRegisterFirstFrameListener(activity: Activity) {
        if (!firstFrameShowed && activity is IFirstFramePage) {
            activity.window.decorView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                if (!firstFrameShowed) {
                    firstFrameShowed = true
                    appLifeListener.afterFirstFrame(instance)
                    handler.postDelayed({ appLifeListener.afterFirstFrame3S(instance) }, 3000)
                }
            }
        }
    }
}