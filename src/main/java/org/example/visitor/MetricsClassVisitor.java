package org.example.visitor;

import org.example.metrics.ClassInfo;
import org.example.metrics.MetricsContext;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

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
        currentClassInfo = metricsContext.getOrCreateClass(name, superName);
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (currentClassInfo != null) {
            currentClassInfo.fieldCount++;
        }
        return super.visitField(access, name, desc, signature, value);
    }
}

