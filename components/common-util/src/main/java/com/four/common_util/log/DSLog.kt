package com.four.common_util.log

import java.lang.reflect.Proxy

/**
 * 常规日志输出，自定义log请参照BaseLogger
 */
object DSLog : ILogger {

    private val loggerManager = LoggerManager()

    private val logger: ILogger = (Proxy.newProxyInstance(DSLogger::class.java.classLoader,
        arrayOf(ILogger::class.java), loggerManager)) as ILogger

    fun d(msg: String) {
        def().debug(msg)
    }

    fun d(tag: String, msg: String) {
        def().debug(tag, msg)
    }

    override fun def(): DSLogger = logger.def()

    override fun net(): DSLogger  = logger.net()

    override fun map(): DSLogger = logger.map()

    fun getLoggerManager() = loggerManager
}