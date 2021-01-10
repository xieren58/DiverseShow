package com.four.app_init_handler.api

object AppLifeEvent {

    /**
     * app attachContext() 之前
     */
    const val BEFORE_ATTACH_CONTEXT = 8

    /**
     * app attachContext()
     */
    const val ATTACH_CONTEXT = 9

    /**
     * app onCreate()
     */
    const val ON_CREATE = 10

    /**
     * 首帧之后
     */
    const val AFTER_FIRST_FRAME = 11

    /**
     * 首帧后3s
     */
    const val AFTER_FIRST_FRAME_3S = 12
}