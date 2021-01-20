package com.four.buildsrc.hotfix


import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import java.util.*


class HotfixClassVisitor(private val classWriter: ClassWriter): ClassVisitor(
    Opcodes.ASM8,
    classWriter
) {

    private companion object {
        private const val CLASS_FIX_ANNOTATION = "Lcom/ds/hotfix/HotFix;"
        private const val METHOD_FIX_ANNOTATION = "Lcom/ds/hotfix/FixModifier;"
        private const val METHOD_ADD_ANNOTATION = "Lcom/ds/hotfix/FixAdd;"
    }
    private var isFieldExist: Boolean = false
    private var owner: String = ""
    private var isInterface: Boolean = false
    private var fileName: String = ""
    private var isHaveCompanion: Boolean = false
    private var isFixClass = false
    private val classSets: HashSet<String> by lazy {
        HashSet<String>()
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(Opcodes.V1_8, access, name, signature, superName, interfaces)
        owner = name
        isInterface = access and Opcodes.ACC_INTERFACE != 0
    }

    override fun visitSource(source: String, debug: String?) {
        super.visitSource(source, debug)
        fileName = source
        //为避免属性重复添加 在此执行属性添加逻辑
        //同时为了添加方法前的判断逻辑 先添加字段
    }

    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
        if (descriptor == CLASS_FIX_ANNOTATION) {
            isFixClass = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitInnerClass(
        name: String,
        outerName: String,
        innerName: String,
        access: Int
    ) {
        super.visitInnerClass(name, outerName, innerName, access)
        if (innerName == "companion") {
            isHaveCompanion = true
        }
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        if (name == "changeQuickRedirect") {
            isFieldExist = true
        }
        return super.visitField(access, name, descriptor, signature, value)
    }

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        println("==================================")
        println("access:$access \n" +
                "name:$name \n" +
                "descriptor:$descriptor \n" +
                "signature:$signature")

        if (name == "<clinit>" && !fileName.endsWith(".kt")) {
            return super.visitMethod(access, name, descriptor, signature, exceptions)
        }
        val methodVisitor = classWriter.visitMethod(access, name, descriptor, signature, exceptions)

        if (isFixClass) {
            return FixFilterMethodVisitor(classSets, api, methodVisitor, access, name, descriptor)
        }
        if (name == "<clinit>") {
            return StaticCodeMethodVisitor(owner, api, methodVisitor, access, name, descriptor)
        }
        return FixInjectMethodVisitor(owner, fileName, api, methodVisitor, access, name, descriptor)
    }

    override fun visitEnd() {
        super.visitEnd()
        //为Kotlin文件生成Companion内部类 存放静态字段
        if(fileName.endsWith(".kt")) {
            if (!isHaveCompanion) {
                println("-------visitEnd 主动生成内部类---------")
                classWriter.visitInnerClass(
                    "$owner${'$'}Companion",
                    owner,
                    "Companion",
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC
                )
                isHaveCompanion = true
            }

            //kotlin文件中 生成内部类Companion引用
            if (isHaveCompanion) {
                val fieldVisitor0 = classWriter.visitField(
                    Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                    "Companion",
                    "L$owner${'$'}Companion;",
                    null,
                    null
                )
                fieldVisitor0.visitEnd()
            }
        }

        //为文件注入changeQuickRedirect字段
        if(!isFieldExist) {
            val fieldVisitor = cv.visitField(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;",
                null,
                null
            )
            //java编码规范 可空变量使用Nullable注解标记
            if (!fileName.endsWith(".kt")) {
                val annotationVisitor = fieldVisitor.visitAnnotation(
                    "Lorg/jetbrains/annotations/Nullable;", false
                )
                annotationVisitor.visitEnd()
            }
            fieldVisitor.visitEnd()
            isFieldExist = true
            println("------------$owner changeQuickRedirect字段 visit注入完成----------------------")
        }
    }
}