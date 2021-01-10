package com.four.app_init_handler.api

/**
 * 详情请见:
 * @see AppLifeEvent
 */
interface IAppLife {

    fun beforeAttachContext(context: Any)

    fun attachContext(context: Any)

    fun onCreate(context: Any)

    fun afterFirstFrame(context: Any)

    fun afterFirstFrame3S(context: Any)
}