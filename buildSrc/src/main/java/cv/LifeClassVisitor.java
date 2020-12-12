package cv;

import com.four.buildsrc.hotfix.HelloWorldClassVisitor;
import com.four.buildsrc.util.Logger;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class LifeClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public LifeClassVisitor(ClassVisitor cv) {
        super(ASM8, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        //System.out.println("LifecycleClassVisitor : visit -----> started ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        //System.out.println("LifecycleClassVisitor : visitMethod : " + name);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        Logger.INSTANCE.log("--------------------visitMethod-----------------------------");
        return new HelloWorldClassVisitor.HelloWorldMethodVisitor(api,mv,access, name, desc);
    }

    @Override
    public void visitEnd() {
        //System.out.println("LifecycleClassVisitor : visit -----> end");
        super.visitEnd();
    }
}