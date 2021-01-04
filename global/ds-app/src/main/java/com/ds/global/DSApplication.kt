package com.ds.global

import android.app.Application

class DSApplication : Application() {

    companion object {

        lateinit var instance: DSApplication
            private set
    }

    init {
        instance = this
    }
}