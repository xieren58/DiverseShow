package com.four.app_init_handler.api

import androidx.annotation.IntDef

@IntDef(AppLifeEvent.BEFORE_ATTACH_CONTEXT, AppLifeEvent.ATTACH_CONTEXT,
    AppLifeEvent.ON_CREATE, AppLifeEvent.AFTER_FIRST_FRAME, AppLifeEvent.AFTER_FIRST_FRAME_3S)
@Retention(AnnotationRetention.SOURCE)
annotation class AppLifeEventInt
