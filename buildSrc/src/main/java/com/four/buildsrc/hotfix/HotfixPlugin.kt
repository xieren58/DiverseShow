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
import org.apache.commons.io.FileUtils
import org.gradle.api.file.Directory
import java.io.File

class HotfixPlugin: BaseTransform(),Plugin<Project> {
    private var hotfixOutputPath = ""
    private var className = ""
    override fun apply(target: Project) {
        hotfixOutputPath = "${target.rootDir}${HOTFIX_OUTPUT_DIR}"
        val dirFile = File(hotfixOutputPath)
        if (dirFile.exists()) {
            FileUtils.deleteDirectory(dirFile)
        }
        if (!PluginSwitch.Hotfix.isOpenHotfix(target)) {
            return
        }
        Logger.log("-----------------$name apply--------------------")

        dirFile.mkdirs()
        //register transform
        val extension: BaseExtension = target.extensions.getByType(BaseExtension::class.java)
        if(extension is LibraryExtension) {
            isLibraryModule = true
        }
        extension.registerTransform(this)
    }

    override fun getName(): String {
        return "HotfixPlugin"
    }

    override fun getClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return HotfixClassVisitor(classWriter)
    }

    override fun copyTargetFilePath(): String {
        if(className.isNotEmpty()) {
            println("----------copyTargetFile:${hotfixOutputPath}/${className}")
            return "${hotfixOutputPath}/$className"
        }
        return ""
    }

    override fun isNeedTraceClass(name: String): Boolean {
        className = name
        var newName = name
        if (name.contains('/')) {
            val index = name.lastIndexOf('/') + 1
            if (index < name.length) {
                newName = name.substring(index)
            }
        }
        if(newName.endsWith(".class")) {
            newName = newName.substring(0,newName.length-6)
        }
        if (newName.equals("BuildConfig") || newName.equals("FixTest")) {
            return true
        }
        return false
    }

    companion object {
        const val HOTFIX_OUTPUT_DIR = "/hotfixOutputs/classes"
    }
}