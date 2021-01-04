package com.four.common_util.log

import com.four.common_util.log.annotation.Config

/**
 * 需要增加新的日志打印器请在这里申明
 * 打印器默认的前缀就是: ds-方法名
 */
interface ILogger {

    @Config(openLog = true, logLevel = LogLevel.DEBUG, name = "def")
    fun def() : DSLogger

    @Config(openLog = true, logLevel = LogLevel.DEBUG, name = "net")
    fun net() : DSLogger

    @Config(openLog = true, logLevel = LogLevel.DEBUG, name = "map")
    fun map() : DSLogger
}