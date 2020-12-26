package com.four.common_util.schedule

import android.os.Handler
import android.os.Looper

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