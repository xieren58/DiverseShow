package com.four.buildsrc.compile

import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 为每个project添加task
 */
class AssembleDebugForAarPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        DepInterceptor.rootProject = DepInterceptor.rootProject ?: findRootProject(target)
        DepInterceptor.openAarRun = try {
            val run = target.properties["compile.openAarRun"].toString().toBoolean()
            println("aar run $run")
            run
        } catch (e: Exception) {
            false
        }

        target.afterEvaluate {
            val rootProject = findRootProject(target)
            //根project则是所有都添加task，反之只添加当前
            if (rootProject == target) {
                target.allprojects {
                    this.afterEvaluate {
                        if (this.plugins.hasPlugin(LibraryPlugin::class.java)) {
                            addTaskToSubObject(this)
                        }
                    }
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