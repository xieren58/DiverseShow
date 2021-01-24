package com.four.app_init_transform

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.four.app_init_handler.StringConstant
import com.four.app_init_handler.abs.AbsTask
import com.google.common.collect.ImmutableSet
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOCase
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.jar.JarFile

open class TaskHoldersHandlerTransform : Transform() {

    companion object {
        const val NAME = "AppInit"

        private val executor = Executors.newFixedThreadPool(6)
    }

    override fun getName(): String = NAME

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental(): Boolean = false

    override fun transform(invocation: TransformInvocation) {
        super.transform(invocation)
        Logger.d("AppInit transform ..")
        val startTime = System.currentTimeMillis()

        val initClassSet: MutableSet<String> = Collections.newSetFromMap(ConcurrentHashMap())
        val prefix = "${StringConstant.APP_INIT_CLASS_PCK
            .replace('.', File.separatorChar)}${File.separator}${StringConstant.TASK_HOLDER_PREFIX}"

        val taskOverCount = AtomicInteger(0)
        var taskCount = 0
        invocation.inputs.forEach {
            //并发流处理
            it.jarInputs.forEach {
                taskCount++
                executor.submit {
                    handleJar(it, invocation, prefix, initClassSet)
                    taskOverCount.getAndIncrement()
                }
            }
            //并发流处理
            it.directoryInputs.forEach {
                taskCount++
                executor.submit {
                    handleDir(it, invocation, prefix, initClassSet)
                    taskOverCount.getAndIncrement()
                }
            }
        }

        val loopStart = System.currentTimeMillis()
        while (taskCount != taskOverCount.get()) {
            //空转就好，因为扫描是非常省时的
            if (System.currentTimeMillis() - loopStart > 5000) {
                throw RuntimeException("loop timeout !!!")
            }
        }

        Logger.d("扫描时长 ${(System.currentTimeMillis() - startTime).toFloat()/1000f}s")
        val dest = invocation.outputProvider.getContentLocation(
            StringConstant.TASK_HOLDERS_HANDLER_NAME, TransformManager.CONTENT_CLASS,
            ImmutableSet.of(QualifiedContent.Scope.PROJECT), Format.DIRECTORY
        )
        generateServiceInitClass(dest.absolutePath, initClassSet)
    }

    private fun getJavaNameFromClassName(name: String) =
        name.substring(0, name.length - SdkConstants.DOT_CLASS.length)
            .replace('/', '.')

    private fun generateServiceInitClass(directory: String, classes: Set<String>) {
        if (classes.isEmpty()) {
            return
        }
        try {
            val ms = System.currentTimeMillis()
            val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
            val cv: ClassVisitor = object : ClassVisitor(Opcodes.ASM5, writer) {}
            val className = "${StringConstant.APP_INIT_CLASS_PCK
                .replace('.', File.separatorChar)}${File.separator}${StringConstant.TASK_HOLDERS_HANDLER_NAME}"
            cv.visit(50, Opcodes.ACC_PUBLIC, className,
                null, "java/lang/Object", null)
            val mv = cv.visitMethod(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                StringConstant.TASK_HOLDERS_HANDLER_FUN_NAME, "()V", null, null
            )
            mv.visitCode()
            for (clazz in classes) {
                mv.visitMethodInsn(
                    Opcodes.INVOKESTATIC, clazz.replace('.', '/'),
                    AbsTask.STATIC_ADD_SELF_TO_CONTAINER_NAME, "()V", false
                )
            }
            mv.visitMaxs(0, 0)
            mv.visitInsn(Opcodes.RETURN)
            mv.visitEnd()
            cv.visitEnd()
            val dest = File(directory, className + SdkConstants.DOT_CLASS)
            dest.parentFile.mkdirs()
            FileOutputStream(dest).use {
                it.write(writer.toByteArray())
            }
            Logger.d("生成appInit class时长 ${(System.currentTimeMillis()-ms).toFloat()/1000f}s")
        } catch (e: IOException) {
            throw e
        }
    }

    private fun handleJar(jar: JarInput,
                          invocation: TransformInvocation,
                          prefix: String,
                          initClassSet: MutableSet<String>) {
        if (jar.status == Status.REMOVED) {
            return
        }
        val src: File = jar.file
        val dest: File = invocation.outputProvider.getContentLocation(
            jar.name, jar.contentTypes, jar.scopes, Format.JAR
        )
        val jarFile = JarFile(src)
        jarFile.entries().iterator().forEach { entry ->
            val name = entry.name
            if (name.startsWith(prefix) && !name.endsWith("Companion${SdkConstants.DOT_CLASS}")) {
                val target = getJavaNameFromClassName(entry.name)
                initClassSet.add(target)
                Logger.d("找到 $target")
            }
        }
        FileUtils.copyFile(src, dest)
    }

    private fun handleDir(directoryInput: DirectoryInput,
                          invocation: TransformInvocation,
                          prefix: String,
                          initClassSet: MutableSet<String>) {
        val src: File = directoryInput.file
        val srcDirPath = src.absolutePath
        val destDirPath = invocation.outputProvider.getContentLocation(directoryInput.name,
            directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY).absolutePath
        File(destDirPath).mkdirs()
        val pck = File(src, StringConstant.APP_INIT_CLASS_PCK.replace('.', File.separatorChar))
        val changedJars = directoryInput.changedFiles.map { MyPair(it.key.absolutePath, it.value) }
        if (pck.exists() && pck.isDirectory) {
            val files = FileUtils.listFiles(pck,
                SuffixFileFilter(SdkConstants.DOT_CLASS, IOCase.INSENSITIVE), TrueFileFilter.INSTANCE)
            files.forEach { file ->
                if (changedJars.contains(Pair(file.absolutePath, Status.REMOVED))) {
                    return@forEach
                }
                val path = file.absolutePath.replace("${src.absolutePath}${File.separatorChar}", "")
                if (path.startsWith(prefix) && !path.endsWith("Companion${SdkConstants.DOT_CLASS}")) {
                    val target = getJavaNameFromClassName(path)
                    initClassSet.add(target)
                    Logger.d("找到 $target")

                    val destPath = file.absolutePath.replace(srcDirPath, destDirPath)
                    val destFile = File(destPath)
                    FileUtils.touch(destFile)
                    FileUtils.copyFile(file, destFile)
                }
            }
        }
    }

    class MyPair<A, B>(val a: A, val b: B) {
        override fun equals(other: Any?): Boolean {
            return other != null && other is MyPair<*, *> && other.a == a && other.b == b
        }

        override fun hashCode(): Int {
            var result = a.hashCode()
            result = 31 * result + b.hashCode()
            return result
        }
    }
}