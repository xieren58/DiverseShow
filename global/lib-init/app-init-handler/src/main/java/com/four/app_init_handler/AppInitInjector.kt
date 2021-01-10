package com.four.app_init_handler

import com.four.app_init_handler.abs.AbsTask
import com.four.app_init_handler.api.IAppLife


object AppInitInjector: IAppLife {

    var debug = true

    /**
     * 将所有的初始化任务放入容器
     */
    fun init() {
        val clazz: Class<*>?
        try {
            clazz = Class.forName("${StringConstant.APP_INIT_CLASS_PCK}.${StringConstant.TASK_HOLDERS_HANDLER_NAME}")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return
        }
        val method = clazz.getMethod(StringConstant.TASK_HOLDERS_HANDLER_FUN_NAME)
        method.invoke(null)
    }

    override fun beforeAttachContext(context: Any) {
        exec(TaskContainerHelper.beforeAttachContextList, context)
    }

    override fun attachContext(context: Any) {
        exec(TaskContainerHelper.attachContextList, context)
    }

    override fun onCreate(context: Any) {
        exec(TaskContainerHelper.onCreateList, context)
    }

    override fun afterFirstFrame(context: Any) {
        exec(TaskContainerHelper.afterFirstFrameList, context)
    }

    override fun afterFirstFrame3S(context: Any) {
        exec(TaskContainerHelper.afterFirstFrame3SList, context)
    }

    private fun exec(list: List<AbsTask>?, context: Any) {
        list ?: return

        list.sortedBy { it.priority }
        list.forEach {
            it.doExec(context)
        }
    }
 }