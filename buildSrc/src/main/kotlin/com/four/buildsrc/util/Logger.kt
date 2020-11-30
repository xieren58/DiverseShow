package com.four.buildsrc.util

import org.gradle.api.Project
import com.four.buildsrc.Constant

object Logger {

    var openLog = false

    fun init(project: Project) {
        openLog =  PropertiesUtil.getBooleanProperty(Constant.PROPERTY_OPEN_LOG, openLog, project)
    }

    fun log(msg: String) {
        if (openLog) {
            println(msg)
        }
    }
}