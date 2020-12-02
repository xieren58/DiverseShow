package com.four.buildsrc.compile

import com.four.buildsrc.compile.json.DepConstant
import com.four.buildsrc.compile.json.DepJsonParser
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.accessors.runtime.addExternalModuleDependencyTo
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
    fun interceptImplProject(handlerScope: DependencyHandlerScope, path: String) : Boolean {
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

            println("aar dir: $buildAarPath")

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
                        interceptImplRepo(handlerScope, "${it.group}:${it.name}:${it.version}")
                    }
                    DepConstant.Ext.PROJECT -> {
                        interceptImplProject(handlerScope, it.projectPath!!)
                    }
                    else -> {
                        addExternalModuleDependencyTo(
                            handlerScope.dependencies, "implementation",
                            it.group, name, it.version, null, null,
                            it.ext, null
                        )
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
            addExternalModuleDependencyTo(
                handlerScope.dependencies, "implementation",
                DepConstant.Default.GROUP, name, DepConstant.Default.VERSION, null,
                null, DepConstant.Ext.AAR, null
            )

            true
        } else false
    }

    fun interceptImplAar(handlerScope: DependencyHandlerScope,
                         name: String,
                         group: String = "",
                         version: String? = null) : Boolean {
        return false
    }

    fun interceptImplRepo(handlerScope: DependencyHandlerScope, variant: String) : Boolean {
        return false
    }

    fun interceptTestImpl(handlerScope: DependencyHandlerScope, variant: String) : Boolean {
        return false
    }

    fun interceptAndroidTestImpl(handlerScope: DependencyHandlerScope, variant: String) : Boolean {
        return false
    }
}