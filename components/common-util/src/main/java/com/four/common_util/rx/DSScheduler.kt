package com.four.common_util.rx

import android.os.Handler
import android.os.Looper
import com.four.app_init_handler.api.AppLifeEvent
import com.four.app_init_handler.api.OnAppLifeChanged
import com.four.common_util.log.DSLog
import io.reactivex.plugins.RxJavaPlugins

private val mainHandler = Handler(Looper.getMainLooper())

fun postUIThread(task: Runnable) {
    mainHandler.post(task)
}

fun runUIThread(task: Runnable) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        task.run()
    } else {
        mainHandler.post(task)
    }
}

object DSScheduler {

    /**
     * 捕捉所有RxJava产生的异常
     */
    @OnAppLifeChanged(AppLifeEvent.BEFORE_ATTACH_CONTEXT, priority = Int.MAX_VALUE)
    fun init() {
        DSLog.def().debug("rx init, setErrorHandler.")
        RxJavaPlugins.setErrorHandler {
            DSLog.def().error("on rx exception:")
            DSLog.def().error("${it?.message}")
            it.printStackTrace()
        }
    }
}