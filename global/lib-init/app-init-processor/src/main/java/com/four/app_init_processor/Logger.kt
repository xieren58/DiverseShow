package com.four.app_init_processor

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

object Logger {
    lateinit var realLogger: Messager

    fun d(msg: String) {
        realLogger.printMessage(Diagnostic.Kind.NOTE, "$msg\r\n")
    }

    fun e(msg: String) {
        realLogger.printMessage(Diagnostic.Kind.ERROR, "$msg\r\n")
    }
}