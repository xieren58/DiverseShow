package com.four.common_util.log

import com.four.common_util.log.annotation.Config

/**
 * 需要增加新的日志打印器请在这里申明
 * 打印器默认的前缀就是方法的名字
 */
interface ILogger {

    @Config(openLog = true, logLevel = LogLevel.DEBUG)
    fun default() : DSLogger

    @Config(openLog = true, logLevel = LogLevel.DEBUG)
    fun net() : DSLogger

    @Config(openLog = true, logLevel = LogLevel.DEBUG)
    fun map() : DSLogger
}