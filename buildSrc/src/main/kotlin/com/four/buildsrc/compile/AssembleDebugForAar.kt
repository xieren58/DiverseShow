package com.four.buildsrc.compile

import com.android.build.gradle.internal.plugins.AppPlugin
import com.four.buildsrc.compile.json.DepConstant
import com.four.buildsrc.util.*
import org.gradle.api.DefaultTask
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency
import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.tasks.TaskAction
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * 拷贝aar，并且将依赖库生成json文件
 * aar的版本指定为1.0
 */
open class AssembleDebugForAar : DefaultTask() {

    companion object {
        const val NAME = "assembleDebugForAar"

        const val BUILD_AAR_DIR = "/compile/aars"

        const val BUILD_JSON_DIR = "/compile/jsons"
    }

    init {
        enabled =
            if (project != project.rootProject && !project.plugins.hasPlugin(AppPlugin::class.java)) {
                this.dependsOn("${project.path}:assembleDebug")
                true
            } else {
                false
            }

        group = "aarrun"
    }

    /**
     * 简化版，project依赖就不考虑版本问题
     */
    @TaskAction
    fun doAction() {
        val copyAarPath = "${project.buildDir}/outputs/aar/${project.name}-debug.aar"
        val buildAarPath = "${project.rootDir}$BUILD_AAR_DIR/"
        val buildJsonPath = "${project.rootDir}$BUILD_JSON_DIR"
        val aarPath = "$buildAarPath/${project.name}-${DepConstant.Default.VERSION}.aar"
        val depJsonPath = "$buildJsonPath/${project.name}-${DepConstant.Default.VERSION}.json"

        if (!File(copyAarPath).exists()) {
            Logger.log("${project.name} aar file not find!!!")
            Logger.log("${project.name} aar file not find!!!")
            Logger.log("${project.name} aar file not find!!!")
            return
        }

        //将aar copy到/aar_build
        FileUtil.copyFileByOverlay(aarPath, copyAarPath)

        val target = JSONObject()
        //扫描implementation
        target.put(
            DepConstant.IMPLEMENTATION_NAME,
            createJSONObjects("implementation"))
        //扫描testImplementation
        target.put(
            DepConstant.TEST_IMPLEMENTATION_NAME,
            createJSONObjects("testImplementation"))
        //扫描androidTestImplementation
        target.put(
            DepConstant.ANDROID_TEST_IMPLEETATION_NAME,
            createJSONObjects("androidTestImplementation"))
        //写入json
        FileUtil.writeStringByOverlay(depJsonPath, target.toString())
    }

    private fun createJSONObjects(configName: String): JSONArray {
        val implArray = JSONArray()
        project.configurations.asMap[configName]?.allDependencies?.apply {
            var index = 0
            forEach {
                val depObj = JSONObject(this.size)
                depObj.put(DepConstant.DEP_GROUP, it.group)
                depObj.put(DepConstant.DEP_NAME, it.name)
                if (it.version.isNullOrEmpty() || it.version == "unspecified") {
                    depObj.put(DepConstant.DEP_VERSION, DepConstant.Default.VERSION)
                } else {
                    depObj.put(DepConstant.DEP_VERSION, it.version)
                }

                //暂时只考虑了aar project jar repo依赖的情况
                //aar 的版本只为1.0
                when (it) {
                    is DefaultProjectDependency -> {
                        depObj.put(DepConstant.DEP_GROUP, DepConstant.Default.GROUP)
                        depObj.put(DepConstant.DEP_EXT, DepConstant.Ext.PROJECT)
                        depObj.put(DepConstant.DEP_PROJECT_PATH, it.dependencyProject.path)
                    }
                    is DefaultExternalModuleDependency -> {
                        if (it.artifacts.size != 0)  {
                            it.artifacts.forEach out@ { art ->
                                depObj.put(DepConstant.DEP_EXT, art.type)
                                return@out
                            }
                        } else {
                            depObj.put(DepConstant.DEP_EXT, DepConstant.Ext.REPO)
                        }
                        depObj.put(DepConstant.DEP_PROJECT_PATH, "")
                    }
                    else -> {
                        depObj.put(DepConstant.DEP_EXT, DepConstant.Ext.REPO)
                        depObj.put(DepConstant.DEP_PROJECT_PATH, "")
                    }
                }
                implArray.put(index++, depObj)
            }
        }
        return implArray
    }
}