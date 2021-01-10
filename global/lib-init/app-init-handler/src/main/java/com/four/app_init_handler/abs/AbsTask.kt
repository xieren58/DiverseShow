package com.four.app_init_handler.abs

import com.four.app_init_handler.AppInitInjector

abstract class AbsTask(val priority: Int) {

    companion object {

        const val TAG = "appInitTask"

        const val EXEC_NAME = "exec"

        const val STATIC_ADD_SELF_TO_CONTAINER_NAME = "addSelfToContainer"
    }

    fun doExec(context: Any) {
        if (AppInitInjector.debug) {
            exec(context)
        } else {
            val startTime = System.currentTimeMillis()
            try {
                exec(context)
            } catch (e: Exception) {
                val endTime = System.currentTimeMillis()
                println("$TAG: init failed!!! spent time = ${endTime-startTime}ms")
                throw e
            }

            //似乎在程序初始化的时候并不能使用这个
            val endTime = System.currentTimeMillis()
            println("$TAG: spent time = ${endTime-startTime}ms")
        }
    }

    abstract fun exec(context: Any)
}