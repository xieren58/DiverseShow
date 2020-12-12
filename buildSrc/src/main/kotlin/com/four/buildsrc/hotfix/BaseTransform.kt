package com.four.buildsrc.hotfix

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

abstract class BaseTransform() : Transform() {
    companion object {
        private const val TAG = "BaseTransform"
    }
    override fun getName(): String {
        return TAG
    }


    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        //需要处理的数据类型,这里表示class文件
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        //是否增量编译
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        //作用范围
        return TransformManager.SCOPE_FULL_PROJECT
    }


    protected var isLibraryModule: Boolean = false

    abstract fun getClassVisitor(classWriter: ClassWriter): ClassVisitor

    private val mWaitableExecutor by lazy {
        WaitableExecutor.useGlobalSharedThreadPool().apply {
            waitForTasksWithQuickFail<Any>(true)
        }
    }

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)
        println("-----------------$name start--------------------")
        val startTime = System.currentTimeMillis()
        val transformOutputProvider = transformInvocation.outputProvider
        val isIncremental = transformInvocation.isIncremental

        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                //处理jar
                mWaitableExecutor.execute {
                    processJarInput(jarInput, transformOutputProvider,isIncremental)
                }
            }

            input.directoryInputs.forEach { directoryInput ->
                //处理源码文件
                mWaitableExecutor.execute {
                    processDirectoryInput(directoryInput, transformOutputProvider,isIncremental)
                }
            }
        }
        mWaitableExecutor.waitForTasksWithQuickFail<Any>(true)
        val currTime = System.currentTimeMillis()
        println("-----------------$name 执行耗时为 ${(currTime - startTime) / 1000} s--------------------")
    }

    private fun processDirectoryInput(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider,isIncremental: Boolean) {
        val directory = outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,Format.DIRECTORY)
        FileUtils.forceMkdir(directory)
        if (isIncremental) {
            val srcPath = directoryInput.file.absolutePath
            val destPath = directory.absolutePath
            val fileStatusMap = directoryInput.changedFiles
            fileStatusMap.entries.forEach { changeFile ->
                val status = changeFile.value
                val inputFile = changeFile.key
                val destFilePath = inputFile.absolutePath.replace(srcPath,destPath)
                val destFile = File(destFilePath)
                when (status) {
                    Status.NOTCHANGED -> {

                    }
                    Status.CHANGED,Status.ADDED -> {
                        FileUtils.touch(destFile)
                        //单个单个地复制文件
                        transformSingleFile(inputFile, destFile)
                        FileUtils.copyFile(inputFile,destFile)
                    }
                    Status.REMOVED -> {
                        if (destFile.exists()) {
                            FileUtils.forceDelete(destFile)
                        }
                    }
                }
            }
        } else {
            transformDirectory(directoryInput,directory)
        }
    }

    private fun transformDirectory(directoryInput: DirectoryInput,dest: File) {
        if (dest.exists()) {
            FileUtils.forceDelete(dest)
        }
        FileUtils.forceMkdir(dest)
        val extensions = arrayOf("class")
        //递归地去获取该文件夹下面所有的文件
        val fileList = FileUtils.listFiles(directoryInput.file,extensions,true)
        val outputFilePath = dest.absolutePath
        val inputFilePath = directoryInput.file.absolutePath
        fileList.forEach { inputFile ->
            println("inputFile name == ${inputFile.name}")
            val outputFullPath = inputFile.absolutePath.replace(inputFilePath, outputFilePath)
            val outputFile = File(outputFullPath)
            if (inputFile.isDirectory) {
                transformDirectory(inputFile as DirectoryInput,outputFile)
            } else {
                //创建文件
                FileUtils.touch(outputFile)
                //单个单个地复制文件
                transformSingleFile(inputFile, outputFile)
            }
        }
        FileUtils.copyDirectory(directoryInput.file,dest)
    }

    private fun processJarInput(jarInput: JarInput, outputProvider: TransformOutputProvider,isIncremental: Boolean) {
        val dest = outputProvider.getContentLocation(jarInput.name,jarInput.contentTypes,jarInput.scopes,Format.JAR)
        if (isIncremental) {
            when(jarInput.status) {
                Status.NOTCHANGED -> {
                    return
                }

                Status.ADDED,Status.CHANGED -> {
                    transformJar(jarInput,dest)
                }

                Status.REMOVED -> {
                    if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                }
            }
        } else {
            transformJar(jarInput,dest)
        }
    }

    private fun transformJar(jarInput: JarInput, dest: File) {
        val path = jarInput.file.absolutePath
        //临时文件存放正在操作中的文件
        val tempFile = File(jarInput.file.parent + File.separator + "classes_temp.jar")
        //之前的缓存存在避免重复插桩
        if (tempFile.exists()) {
            tempFile.delete()
        }
        if(path.endsWith(".jar") && !path.startsWith("androidx")) {
            var jarName = jarInput.name
            val md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length - 4)
            }
            val jarFile = JarFile(jarInput.file)
            val jarFileEntries = jarFile.entries()

            val jarOutputStream = JarOutputStream(FileOutputStream(tempFile))

            while (jarFileEntries.hasMoreElements()) {
                val jarEntry = jarFileEntries.nextElement()
                val entryName = jarEntry.name
                val zipEntry = ZipEntry(entryName)
                //读取jar中的输入流
                val inputStream = jarFile.getInputStream(zipEntry)

                if (isNeedTraceClass(entryName)) {
                    //执行插桩
                    println("----------- deal with \"jar\" class file <' + entryName + '> -----------")
                    jarOutputStream.putNextEntry(zipEntry)
                    val classReader = ClassReader(IOUtils.toByteArray(inputStream))
                    val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                    classReader.accept(getClassVisitor(classWriter), ClassReader.EXPAND_FRAMES)
                    jarOutputStream.write(classWriter.toByteArray())
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()
            }
            jarOutputStream.close()
            jarFile.close()
        }
        FileUtils.copyFile(tempFile, dest)
        tempFile.delete()
    }

    private fun transformSingleFile(inputFile: File, destFile: File) {
        println("扫描单个文件")
        if (isNeedTraceClass(destFile.name)) {
            traceFile(inputFile.inputStream(), FileOutputStream(destFile))
        } else {
            FileUtils.copyFile(inputFile, destFile)
        }
    }

    private fun traceFile(inputStream: FileInputStream, outputStream:FileOutputStream) {

        val classReader = ClassReader(inputStream)
        val classWriter = ClassWriter(classReader,ClassWriter.COMPUTE_MAXS)
        classReader.accept(getClassVisitor(classWriter), ClassReader.EXPAND_FRAMES)
        outputStream.write(classWriter.toByteArray())

        inputStream.close()
        outputStream.close()
    }

    private fun isNeedTraceClass(name: String):Boolean {
        val result = name.endsWith(".class") &&
                !(name.startsWith("R$") || name.startsWith("R.") || name.startsWith("androidx/"))
        println("输入文件为：$name,isNeedTraceClass: $result")
        return result
    }
}