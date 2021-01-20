package com.four.buildsrc.hotfix

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import java.util.HashSet

//用于热修包删除多余方法 保留修复方法及新增方法
class FixFilterMethodVisitor(private val set: HashSet<String>,
                             api: Int,
                             methodVisitor: MethodVisitor,
                             access: Int,
                             name: String,
                             descriptor: String
) : MethodVisitor(api, methodVisitor) {

    private companion object {
        private const val METHOD_FIX_ANNOTATION = "Lcom/ds/hotfix/FixModifier;"
        private const val METHOD_ADD_ANNOTATION = "Lcom/ds/hotfix/FixAdd;"
    }

    private var isFixFilter = false
    override fun visitAnnotation(descriptor: String, visible: Boolean): AnnotationVisitor {
        isFixFilter = false
        if (descriptor == METHOD_FIX_ANNOTATION || descriptor == METHOD_ADD_ANNOTATION) {
            isFixFilter = true
        }
        return super.visitAnnotation(descriptor, visible)
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String,
        isInterface: Boolean
    ) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        if (isFixFilter) {
            set.add(descriptor)
        }
    }
}