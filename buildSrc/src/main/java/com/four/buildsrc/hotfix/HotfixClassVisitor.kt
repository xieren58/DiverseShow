package com.four.buildsrc.hotfix


import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter


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
        println("visitAnnotation: $descriptor")
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
    ): MethodVisitor? {
        val methodVisitor = classWriter.visitMethod(
            access,
            name,
            descriptor,
            signature,
            exceptions
        )

        if(name == "<clinit>") {
            return StaticCodeMethodVisitor(owner, api, methodVisitor, access, name, descriptor)
        }

        if (isFixClass) {
            return FixFilterMethodVisitor(api, methodVisitor, access, name, descriptor)
        }

        return FixInjectMethodVisitor(owner, fileName, api, methodVisitor, access, name, descriptor)
    }

    override fun visitEnd() {
        super.visitEnd()
        //为Kotlin文件生成Companion内部类 存放静态字段
        if(fileName.endsWith(".kt") && !isHaveCompanion) {
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
        if(fileName.endsWith(".kt") && isHaveCompanion) {
            val fieldVisitor0 = classWriter.visitField(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL or Opcodes.ACC_STATIC,
                "Companion",
                "L$owner${'$'}Companion;",
                null,
                null
            )
            fieldVisitor0.visitEnd()
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

    //用于热修注入字段及热修执行逻辑
    class FixInjectMethodVisitor(
        private val owner: String,
        private val fileName: String,
        api: Int, methodVisitor: MethodVisitor,
        access: Int, name: String, descriptor: String
    )
        : AdviceAdapter(api, methodVisitor, access, name, descriptor) {
        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
            if(descriptor == METHOD_FIX_ANNOTATION
                || descriptor == METHOD_ADD_ANNOTATION) {
                println("current file owner: ${owner},source: $fileName")
            }
            return super.visitAnnotation(descriptor, visible)
        }

        override fun onMethodEnter() {
            super.onMethodEnter()
            println("------------changeQuickRedirect 方法内判空执行----------------------")
            val label = Label()
            mv.visitLabel(label)
            mv.visitLineNumber(10, label)
            mv.visitTypeInsn(NEW, "com/ds/hotfix/FixProxy")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "com/ds/hotfix/FixProxy",
                "<init>",
                "()V",
                false
            )
            mv.visitFieldInsn(
                PUTSTATIC,
                owner,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            val label0 = Label()
            mv.visitLabel(label0)
            mv.visitFieldInsn(
                GETSTATIC,
                owner,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            val label1 = Label()
            mv.visitJumpInsn(IFNULL, label1)
            val label2 = Label()
            mv.visitLabel(label2)
            mv.visitFieldInsn(
                GETSTATIC,
                owner,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            mv.visitLdcInsn("test")
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(
                INVOKEINTERFACE,
                "com/ds/hotfix/ChangeQuickRedirect",
                "isSupport",
                "(Ljava/lang/String;Ljava/lang/Object;)Z",
                true
            )
            mv.visitJumpInsn(IFEQ, label1)
            val label3 = Label()
            mv.visitLabel(label3)
            mv.visitFieldInsn(
                GETSTATIC,
                owner,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            mv.visitLdcInsn("test")
            mv.visitInsn(ACONST_NULL)
            mv.visitMethodInsn(
                INVOKEINTERFACE,
                "com/ds/hotfix/ChangeQuickRedirect",
                "accessDispatch",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;",
                true
            )
            mv.visitInsn(POP)
            val label4 = Label()
            mv.visitLabel(label4)
            mv.visitLdcInsn("as")
            mv.visitLdcInsn("fix invoke!")
            mv.visitMethodInsn(
                INVOKESTATIC,
                "android/util/Log",
                "e",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false
            )
            mv.visitInsn(POP)
            mv.visitLabel(label1)
            mv.visitFrame(F_SAME, 0, null, 0, null)
            mv.visitInsn(RETURN)
            val label5 = Label()
            mv.visitLabel(label5)
            mv.visitLocalVariable("this", "L$owner;", null, label0, label5, 0)
            mv.visitMaxs(3, 1)
            mv.visitEnd()
        }

        override fun onMethodExit(opcode: Int) {
            super.onMethodExit(opcode)
        }
    }

    //用于注入静态字段
    class StaticCodeMethodVisitor(
        private val owner: String,
        api: Int, methodVisitor: MethodVisitor,
        access: Int, name: String, descriptor: String
    ):
        AdviceAdapter(api, methodVisitor, access, name, descriptor) {

        override fun onMethodEnter() {
            super.onMethodEnter()
            mv.visitTypeInsn(NEW, "$owner${'$'}Companion")
            mv.visitInsn(DUP)
            mv.visitInsn(ACONST_NULL)
            mv.visitMethodInsn(
                INVOKESPECIAL,
                "$owner${'$'}Companion",
                "<init>",
                "(Lkotlin/jvm/internal/DefaultConstructorMarker;)V",
                false
            )
            mv.visitFieldInsn(
                PUTSTATIC,
                owner,
                "Companion",
                "L$owner${'$'}Companion;"
            )
            mv.visitInsn(RETURN)
            mv.visitMaxs(3, 0)
            mv.visitEnd()
        }
    }

    //用于热修包删除多余方法 保留修复方法及新增方法
    class FixFilterMethodVisitor(api: Int,
                                 methodVisitor: MethodVisitor,
                                 access: Int,
                                 name: String,
                                 descriptor: String
    ) : MethodVisitor(api, methodVisitor) {
        private var isFixFilter = false
        override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor? {
            println("FixFilterMethodVisitor visitAnnotation:$descriptor")
            if (descriptor == METHOD_FIX_ANNOTATION || descriptor == METHOD_ADD_ANNOTATION) {
                isFixFilter = true
                return super.visitAnnotation(descriptor, visible)
            }
            return null
        }

        override fun visitMethodInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            if (!isFixFilter) {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
            }
        }

    }
}