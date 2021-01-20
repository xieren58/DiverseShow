package com.four.app_init_transform

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class AppInitPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.extensions.getByType(BaseExtension::class.java).registerTransform(TaskHoldersHandlerTransform())
        println("register TaskHoldersHandlerTransform..")
    }
}