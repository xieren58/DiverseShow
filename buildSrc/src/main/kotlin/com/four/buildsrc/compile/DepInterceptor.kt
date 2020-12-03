package com.four.buildsrc.compile

import com.four.buildsrc.compile.json.DepConstant
import com.four.buildsrc.compile.json.DepJsonParser
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo
import org.gradle.kotlin.dsl.project
import java.io.File

/**
 * 依赖拦截
 */
object DepInterceptor {

    // 需要初始化
    var rootProject: Project? = null

    var openAarRun = true

    /**
     * 目前只拦截project类型
     */
    fun interceptImplProject(handlerScope: DependencyHandlerScope, path: String, fromKts: Boolean = true) : Boolean {
        val rootProj : Project = rootProject ?: return false
        if (!openAarRun) {
            return false
        }

        var target: Project? = null
        rootProj.allprojects.forEach { project ->
            if (project.path == path) {
                target = project
            }
        }

        //不是来自于脚本，则需要手动添加project
        if (!fromKts && target != null) {
            handlerScope.dependencies.add("implementation",
                handlerScope.dependencies.project(target!!.path))
            return true
        }

        //不存在则依赖它的aar
        return if (target == null) {
            val index = path.lastIndexOf(':')
            if (index < 0 || (index + 1) == path.length) {
                throw RuntimeException("$path, path formatting error.")
            }
            val name = path.substring(index + 1)
            val buildAarPath = "${rootProj.rootDir}${AssembleDebugForAar.BUILD_AAR_DIR}"
            val buildJsonPath = "${rootProj.rootDir}${AssembleDebugForAar.BUILD_JSON_DIR}"
            val aarPath = "$buildAarPath/$name-${DepConstant.Default.VERSION}.aar"
            val depJsonPath = "$buildJsonPath/$name-${DepConstant.Default.VERSION}.json"

            println("dep json dir: $depJsonPath")

            val aarFile = File(aarPath)
            val jsonFile = File(depJsonPath)
            if (!aarFile.exists() || !jsonFile.exists()) {
                throw RuntimeException("aar file or json file is not exist.")
            }

            //依赖子项目的依赖
            val bean = DepJsonParser.parse(jsonFile.readText())
            bean.impls.forEach {
                when (it.ext) {
                    DepConstant.Ext.REPO -> {
                        interceptImplRepo(handlerScope, "${it.group}:${it.name}:${it.version}", fromKts = false)
                    }
                    DepConstant.Ext.PROJECT -> {
                        interceptImplProject(handlerScope, it.projectPath!!, fromKts = false)
                    }
                    else -> {
                        interceptImplAar(handlerScope, it.name, it.group, it.version, fromKts = false)
                    }
                }
            }
            bean.testImpls.forEach {
                interceptTestImpl(handlerScope, "${it.group}:${it.name}:${it.version}")
            }
            bean.androidTestImpls.forEach {
                interceptAndroidTestImpl(handlerScope, "${it.group}:${it.name}:${it.version}")
            }

            //依赖自己的aar
            interceptImplAar(handlerScope, name, DepConstant.Default.GROUP,
                DepConstant.Default.VERSION, fromKts = false)

            true
        } else false
    }

    fun interceptImplAar(handlerScope: DependencyHandlerScope,
                         name: String,
                         group: String = "",
                         version: String? = null,
                         fromKts: Boolean = true) : Boolean {
        if (!fromKts) {
            addExternalModuleDependencyTo(
                handlerScope.dependencies, "implementation",
                group, name, version, null, null,
                DepConstant.Ext.AAR, null)
            return true
        }
        return false
    }

    fun interceptImplRepo(handlerScope: DependencyHandlerScope,
                          variant: String,
                          fromKts: Boolean = true) : Boolean {
        return false
    }

    fun interceptTestImpl(handlerScope: DependencyHandlerScope,
                          variant: String,
                          fromKts: Boolean = true) : Boolean {
        return false
    }

    fun interceptAndroidTestImpl(handlerScope: DependencyHandlerScope,
                                 variant: String,
                                 fromKts: Boolean = true) : Boolean {
        return false
    }
}