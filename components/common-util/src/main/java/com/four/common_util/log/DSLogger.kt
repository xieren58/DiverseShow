package com.four.common_util.log

import android.util.Log

class DSLogger(
    var openLog: Boolean = true,
    var logLevel: LogLevel = LogLevel.DEBUG,
    val name: String = "ds"
) {

    init {
        if (logLevel.isLessThanOrEqual(LogLevel.NONE)) {
            openLog = false
        }
    }

    fun debug(msg: String) : DSLogger {
        if (openLog && logLevel.isLessThan(LogLevel.INFO)) {
            Log.d(name, msg)
        }
        return this
    }

    fun debug(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isLessThan(LogLevel.INFO)) {
            Log.d("$name-$tag", msg)
        }
        return this
    }

    fun info(msg: String) : DSLogger {
        if (openLog && logLevel.isLessThan(LogLevel.ERROR)) {
            Log.i(name, msg)
        }
        return this
    }

    fun info(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isLessThan(LogLevel.ERROR)) {
            Log.i("$name-$tag", msg)
        }
        return this
    }

    fun error(msg: String) : DSLogger {
        if (openLog && logLevel.isLessThanOrEqual(LogLevel.ERROR)) {
            Log.i(name, msg)
        }
        return this
    }

    fun error(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isLessThanOrEqual(LogLevel.ERROR)) {
            Log.i("$name-$tag", msg)
        }
        return this
    }
}