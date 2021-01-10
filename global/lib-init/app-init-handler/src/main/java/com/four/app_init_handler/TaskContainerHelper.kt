package com.four.app_init_handler

import com.four.app_init_handler.abs.AbsTask
import com.four.app_init_handler.api.AppLifeEvent
import java.lang.RuntimeException

object TaskContainerHelper {

    var beforeAttachContextList: MutableList<AbsTask>? = null
        private set
    var attachContextList: MutableList<AbsTask>? = null
        private set
    var onCreateList: MutableList<AbsTask>? = null
        private set
    var afterFirstFrameList: MutableList<AbsTask>? = null
        private set
    var afterFirstFrame3SList: MutableList<AbsTask>? = null
        private set

    fun appLifeEventToMethodString(appLife: Int): String {
        return when(appLife) {
            AppLifeEvent.BEFORE_ATTACH_CONTEXT -> "addToBeforeAttachContextList"
            AppLifeEvent.ATTACH_CONTEXT -> "addToAttachContextList"
            AppLifeEvent.ON_CREATE -> "addToOnCreateList"
            AppLifeEvent.AFTER_FIRST_FRAME -> "addToAfterFirstFrameList"
            AppLifeEvent.AFTER_FIRST_FRAME_3S -> "addToAfterFirstFrame3SList"
            else -> throw RuntimeException("not found value=${appLife}, please check AppLifeEvent.")
        }
    }

    fun addToBeforeAttachContextList(task: AbsTask) {
        beforeAttachContextList = beforeAttachContextList ?: mutableListOf()
        beforeAttachContextList!!.add(task)
    }

    fun addToAttachContextList(task: AbsTask) {
        attachContextList = attachContextList ?: mutableListOf()
        attachContextList!!.add(task)
    }
    fun addToOnCreateList(task: AbsTask) {
        onCreateList = onCreateList ?: mutableListOf()
        onCreateList!!.add(task)
    }

    fun addToAfterFirstFrameList(task: AbsTask) {
        afterFirstFrameList = afterFirstFrameList ?: mutableListOf()
        afterFirstFrameList!!.add(task)
    }

    fun addToAfterFirstFrame3SList(task: AbsTask) {
        afterFirstFrame3SList = afterFirstFrame3SList ?: mutableListOf()
        afterFirstFrame3SList!!.add(task)
    }
}