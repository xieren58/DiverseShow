package com.four.common_util.log

import android.util.Log

class DSLogger(
    var openLog: Boolean = true,
    var logLevel: LogLevel = LogLevel.DEBUG,
    val name: String = "dslog"
) {

    init {
        if (!logLevel.isBigger(LogLevel.NONE)) {
            openLog = false
        }
    }

    fun debug(msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.DEBUG)) {
            Log.d(name, msg)
        }
        return this
    }

    fun debug(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.DEBUG)) {
            Log.d("$name-$tag", msg)
        }
        return this
    }

    fun info(msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.INFO)) {
            Log.i(name, msg)
        }
        return this
    }

    fun info(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.INFO)) {
            Log.i("$name-$tag", msg)
        }
        return this
    }

    fun error(msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.ERROR)) {
            Log.i(name, msg)
        }
        return this
    }

    fun error(tag: String, msg: String) : DSLogger {
        if (openLog && logLevel.isBiggerOrEqual(LogLevel.ERROR)) {
            Log.i("$name-$tag", msg)
        }
        return this
    }
}