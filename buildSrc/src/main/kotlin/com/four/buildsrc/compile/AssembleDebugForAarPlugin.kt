package com.four.buildsrc.compile

import com.android.build.gradle.internal.plugins.AppPlugin
import com.android.build.gradle.internal.plugins.LibraryPlugin
import com.four.buildsrc.compile.intercept.DepInterceptHelper
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 为每个project添加task
 *
 * /compile/下的类都是独立存在的，可剥离出去形成库
 */
class AssembleDebugForAarPlugin : Plugin<Project> {

    companion object {
        const val OPEN_AAR_RUN_PROPERTY = "compile.openAarRun"
    }

    override fun apply(target: Project) {
        target.gradle.beforeProject {
            DepInterceptHelper.rootProject = DepInterceptHelper.rootProject ?: findRootProject(target)
        }

        DepInterceptHelper.openAarRun = try {
            val run = target.properties[OPEN_AAR_RUN_PROPERTY].toString().toBoolean()
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