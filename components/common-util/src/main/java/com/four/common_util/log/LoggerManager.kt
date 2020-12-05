package com.four.common_util.log

import android.util.Log
import com.four.common_util.log.annotation.Config
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class LoggerManager : InvocationHandler {

    private val loggers = mutableListOf<DSLogger>()

    private val defaultLogger = DSLogger()

    private val methodOfLoggers = mutableMapOf<String, DSLogger>()

    init {
        methodOfLoggers["DiverseShow-Logger"] = defaultLogger
        loggers.add(defaultLogger)
    }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any {
        if (method.returnType != DSLogger::class.java) {
            throw RuntimeException("find ${method.returnType.simpleName} on ${method.name}'s return type, but require BaseLogger.")
        }

        val fromMethodName = methodOfLoggers[method.name]
        if (fromMethodName != null) {
            return fromMethodName
        } else {
            var targetAnnotation: Config? = null
            method.annotations.forEach find@ {
                if (it is Config) {
                    targetAnnotation = it
                    return@find
                }
            }
            if (targetAnnotation == null) {
                Log.e("ds", "the ${method.name}() has no annotation which is Config::class ")
                return defaultLogger
            }
            val config = targetAnnotation!!
            val logger = DSLogger(config.openLog, config.logLevel)
            synchronized(this::class) {
                val checkLogger = methodOfLoggers[method.name]
                if (checkLogger != null) return checkLogger
                methodOfLoggers[method.name] = logger
                loggers.add(logger)
                return logger
            }
        }
    }

    fun closeAllLogger() {
        var index = 0
        val length = loggers.size
        while (index++ < length) {
            loggers[index] .openLog = false
        }
    }

    fun closeLogger(methodName: String) : Boolean {
        val logger = methodOfLoggers[methodName]
        return if (logger != null) {
            logger.openLog = false
            true
        } else {
            false
        }
    }

    fun findLogger(methodName: String) : DSLogger? = methodOfLoggers[methodName]
}
