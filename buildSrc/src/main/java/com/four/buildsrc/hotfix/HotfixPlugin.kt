package com.four.buildsrc.hotfix

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.four.buildsrc.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import com.four.buildsrc.PluginSwitch

class HotfixPlugin: BaseTransform(),Plugin<Project> {
    override fun apply(target: Project) {
        if (!PluginSwitch.Hotfix.isOpenHotfix(target)) {
            return
        }
        Logger.log("-----------------$name apply--------------------")
        //register transform
        var extension: BaseExtension? = null
        //App模块下能拿到AppExtension 但library执行会抛异常 不想为library单独写插件 直接try catch了
        try {
            extension= target.extensions.getByType(AppExtension::class.java)
        }catch (e: UnknownDomainObjectException) {
            println("${target.name}模块查找 AppExtension失败 开始查找LibraryExtension")
        }
        if(extension == null) {
            extension = target.extensions.getByType(LibraryExtension::class.java)
            isLibraryModule = true
        }
        extension?.registerTransform(this)
    }

    override fun getName(): String {
        return "HotfixPlugin"
    }

    override fun getClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return HotfixClassVisitor(classWriter)
    }

    override fun isNeedTraceClass(name: String): Boolean {
        var newName = name
        if (name.contains('/')) {
            val index = name.lastIndexOf('/') + 1
            if (index+1 < name.length) {
                newName = name.substring(index)
            }
        }
        if(newName.endsWith(".class")) {
            newName = newName.substring(0,newName.length-6)
        }
        println("输入文件为: $newName")
        if (newName.equals("BuildConfig.class") || newName.equals("FixTest.class")) {
            return true
        }
        return false
    }

}