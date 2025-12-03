package org.example.visitor;

import org.example.metrics.model.ClassInfo;
import org.example.metrics.model.MethodInfo;
import org.example.metrics.MetricsContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM8;

public class MetricsClassVisitor extends ClassVisitor {

    private final MetricsContext metricsContext;
    private ClassInfo currentClassInfo;

    public MetricsClassVisitor(MetricsContext metricsContext) {
        super(ASM8);
        this.metricsContext = metricsContext;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        currentClassInfo = metricsContext.getOrCreateClass(name, superName, access);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (currentClassInfo != null) {
            currentClassInfo.incrementFieldCount();
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (currentClassInfo != null) {
            currentClassInfo.addMethodInfo(
                    new MethodInfo(name, desc, access)
            );
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}

