package com.four.app_init_processor

import com.four.app_init_handler.StringConstant
import com.four.app_init_handler.TaskContainerHelper
import com.four.app_init_handler.abs.AbsTask
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.jvm.jvmStatic
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.annotation.processing.Filer

object TaskClassGenerator {

    fun generate(appLifeElement: AppLifeElement, filer: Filer) {
        val packageName = StringConstant.APP_INIT_CLASS_PCK
        val hashName = hash("${appLifeElement.typeElement.qualifiedName}.${appLifeElement.executableElement.simpleName}.${appLifeElement.executableElement.parameters.size}")
        val className = "${StringConstant.TASK_HOLDER_PREFIX}${StringConstant.SPLITTER}$hashName"

        val fileSpec = FileSpec.builder(packageName, className)
            .addImport(AbsTask::class.java.`package`.name, AbsTask::class.simpleName!!)
            .addImport(
                TaskContainerHelper::class.java.`package`.name,
                TaskContainerHelper::class.simpleName!!
            )
            .addType(
                TypeSpec.classBuilder(className)
                    .superclass(AbsTask::class)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("priority", Int::class)
                            .build()
                    )
                    .addSuperclassConstructorParameter("priority")
                    .addFunction(
                        FunSpec.builder(AbsTask.EXEC_NAME)
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("context", Any::class)
                            .addCode(
                                "${appLifeElement.typeElement.qualifiedName}.${appLifeElement.executableElement.simpleName}(${if (appLifeElement
                                        .executableElement.parameters.size == 1) "context as ${appLifeElement.executableElement.parameters[0].asType()}" else ""})"
                            )
                            .build()

                    )
                    .addType(
                        TypeSpec.companionObjectBuilder()
                            .addFunction(
                                FunSpec.builder(AbsTask.STATIC_ADD_SELF_TO_CONTAINER_NAME)
                                    .jvmStatic()
                                    .addCode(
                                        "${TaskContainerHelper::class.simpleName}.${
                                            TaskContainerHelper.appLifeEventToMethodString(
                                                appLifeElement.taskData.appLife
                                            )
                                        }(${className}(${appLifeElement.taskData.priority}))"
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
        fileSpec.build().writeTo(filer)
    }


    private fun hash(str: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray())
            BigInteger(1, md.digest()).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            Integer.toHexString(str.hashCode())
        }
    }
}