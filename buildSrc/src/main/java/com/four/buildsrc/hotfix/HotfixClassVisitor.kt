package com.four.buildsrc.hotfix


import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.util.CheckMethodAdapter

class HotfixClassVisitor(private val classWriter: ClassWriter): ClassVisitor(
    Opcodes.ASM8,
    classWriter
) {

    private var isFieldExist: Boolean = false

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(Opcodes.V1_8, access, name, signature, superName, interfaces)
        //为避免属性重复添加 在此执行属性添加逻辑
        //同时为了添加方法前的判断逻辑 先添加字段
        if (!isFieldExist) {
            val fieldVisitor = cv.visitField(
                Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC,
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;",
                null,
                null
            )
            val annotationVisitor = fieldVisitor.visitAnnotation(
                "Lorg/jetbrains/annotations/Nullable;", false
            )
            annotationVisitor.visitEnd()
            fieldVisitor.visitEnd()
            isFieldExist = true
            println("------------changeQuickRedirect字段 visitMethod注入完成----------------------")
        }
    }

    override fun visitSource(source: String?, debug: String?) {
        super.visitSource(source, debug)
    }

    override fun visitOuterClass(owner: String?, name: String?, descriptor: String?) {
        super.visitOuterClass(owner, name, descriptor)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitTypeAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible)
    }

    override fun visitAttribute(attribute: Attribute?) {
        super.visitAttribute(attribute)
    }

    override fun visitInnerClass(
        name: String?,
        outerName: String?,
        innerName: String?,
        access: Int
    ) {
        super.visitInnerClass(name, outerName, innerName, access)
    }

    override fun visitField(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        if (name.equals("changeQuickRedirect")) {
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
        /*println(
            "HotfixClassVisitor visit--- descriptor:$descriptor-access:$access" +
                    "-name$name-signature$signature-exceptions$exceptions")*/
        if(isFieldExist && !name.equals("<clinit>")) {
            val methodVisitor = classWriter.visitMethod(
                access,
                name,
                descriptor,
                signature,
                exceptions
            )
            return FixCheckMethodVisitor(api, methodVisitor, access, name, descriptor)
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    override fun visitEnd() {
        super.visitEnd()
    }

    class FixCheckMethodVisitor(
        api: Int, methodVisitor: MethodVisitor,
        access: Int, name: String, descriptor: String
    )
        : AdviceAdapter(api, methodVisitor, access, name, descriptor) {

        override fun onMethodEnter() {
            super.onMethodEnter()
            println("------------changeQuickRedirect 方法内判空执行----------------------")
            /*val label0 = Label()
            mv.visitLabel(label0)
            mv.visitFieldInsn(
                GETSTATIC,
                "com/ds/hotfix/FixTest",
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            val label1 = Label()
            mv.visitJumpInsn(IFNULL, label1)
            mv.visitLabel(label1)
            mv.visitFrame(F_SAME, 0, null, 0, null)
            mv.visitInsn(RETURN)
            val label2 = Label()
            mv.visitLabel(label2)
            mv.visitLocalVariable("this", "Lcom/ds/hotfix/FixTest;", null, label0, label2, 0)
            mv.visitMaxs(1, 1)
            mv.visitEnd()*/
            /*mv.visitFieldInsn(
                GETSTATIC,
                "com/ds/hotfix/Fix",
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            mv.visitLdcInsn("test")
            mv.visitInsn(ICONST_0)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
            mv.visitMethodInsn(
                INVOKEINTERFACE,
                "com/ds/hotfix/ChangeQuickRedirect",
                "isSupport",
                "(Ljava/lang/String;[Ljava/lang/Object;)Z",
                true
            )
            mv.visitJumpInsn(IFEQ, label1)
            val label2 = Label()
            mv.visitLabel(label2)
            mv.visitLineNumber(9, label2)
            mv.visitFieldInsn(
                GETSTATIC,
                "com/ds/hotfix/Fix",
                "changeQuickRedirect",
                "Lcom/ds/hotfix/ChangeQuickRedirect;"
            )
            mv.visitLdcInsn("test")
            mv.visitInsn(ICONST_0)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
            mv.visitMethodInsn(
                INVOKEINTERFACE,
                "com/ds/hotfix/ChangeQuickRedirect",
                "accessDispatch",
                "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;",
                true
            )
            mv.visitTypeInsn(CHECKCAST, "java/lang/Integer")
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/Integer",
                "intValue",
                "()I",
                false
            )
            mv.visitInsn(POP)*/
        }

        override fun onMethodExit(opcode: Int) {
            super.onMethodExit(opcode)
        }
    }

}