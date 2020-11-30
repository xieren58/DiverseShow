package com.four.buildsrc.util

import org.gradle.api.Project

object PropertiesUtil {

    fun getBooleanProperty(name: String, def: Boolean, project: Project) : Boolean {
        val value = project.properties[name]
        return if (value == null) {
            def
        } else {
            return try {
                value.toString().toBoolean()
            } catch (e: Exception) {
                def
            }
        }
    }

    fun getStringProperty(name: String, def: String, project: Project) : String {
        val value = project.properties[name]
        return if (value == null) {
            def
        } else {
            return try {
                value.toString()
            } catch (e: Exception) {
                def
            }
        }
    }

    fun getIntProperty(name: String, def: Int, project: Project) : Int {
        val value = project.properties[name]
        return if (value == null) {
            def
        } else {
            return try {
                value.toString().toInt()
            } catch (e: Exception) {
                def
            }
        }
    }
}