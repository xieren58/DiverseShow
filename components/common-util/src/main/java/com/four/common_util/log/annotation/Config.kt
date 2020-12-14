package com.four.common_util.log.annotation

import com.four.common_util.log.LogLevel

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Config(val openLog: Boolean = true,
                        val logLevel: LogLevel = LogLevel.DEBUG,
                        val name: String)
