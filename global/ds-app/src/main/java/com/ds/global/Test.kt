package com.ds.global

import android.content.Context
import android.util.Log
import com.four.app_init_handler.api.AppLifeEvent
import com.four.app_init_handler.api.OnAppLifeChanged

object Test {

    @OnAppLifeChanged(AppLifeEvent.BEFORE_ATTACH_CONTEXT)
    fun a() {
        Log.d("tagg", "BEFORE_ATTACH_CONTEXT")
    }

    @OnAppLifeChanged(AppLifeEvent.ATTACH_CONTEXT)
    fun b() {
        Log.d("tagg", "ATTACH_CONTEXT")
    }

    @OnAppLifeChanged(AppLifeEvent.ON_CREATE)
    fun c() {
        Log.d("tagg", "ON_CREATE")
    }

    @OnAppLifeChanged(AppLifeEvent.AFTER_FIRST_FRAME)
    fun d(context: Context) {
        Log.d("tagg", "AFTER_FIRST_FRAME")
    }

    @OnAppLifeChanged(AppLifeEvent.AFTER_FIRST_FRAME_3S)
    fun e(context: Context) {
        Log.d("tagg", "AFTER_FIRST_FRAME_3S")
    }
}