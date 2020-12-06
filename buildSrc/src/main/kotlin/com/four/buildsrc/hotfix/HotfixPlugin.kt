package com.four.buildsrc.hotfix

import com.android.build.api.transform.*
import com.four.buildsrc.util.Logger
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class HotfixPlugin: AsmTransform(),Plugin<Project> {
    override fun apply(target: Project) {
        Logger.log("-----------------$name apply--------------------")
        //register transform
        val android = target.extensions.getByType(AppExtension::class.java)
        android.registerTransform(this)
    }

    override fun getName(): String {
        return "HotfixPlugin"
    }

    override fun transform(transformInvocation: TransformInvocation) {
        println("-----------------$name start--------------------")
        super.transform(transformInvocation)
    }

    override fun getClassVisitor(classWriter: ClassWriter): ClassVisitor {
        return HotfixClassVisitor(classWriter)
    }

}