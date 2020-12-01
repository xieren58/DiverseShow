package com.four.buildsrc.compile

import com.android.build.gradle.internal.plugins.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 为每个project添加task
 */
class AssembleDebugForAarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            val rootProject = findRootProject(target)
            //根project则是所有都添加task，反之只添加当前
            if (rootProject == target) {
                target.allprojects {
                    addTaskToSubObject(this)
                }
            } else {
                addTaskToSubObject(target)
            }
        }
    }

    private fun findRootProject(project: Project): Project {
        var rootProject = project
        while (rootProject != rootProject.rootProject) {
            rootProject = rootProject.rootProject
        }
        return rootProject
    }

    private fun addTaskToSubObject(project: Project) {
        if (project != project.rootProject
            && !project.plugins.hasPlugin(AppPlugin::class.java)
            && project.tasks.findByName(AssembleDebugForAar.NAME) == null) {
            project.tasks.create(AssembleDebugForAar.NAME, AssembleDebugForAar::class.java)
        }
    }
}