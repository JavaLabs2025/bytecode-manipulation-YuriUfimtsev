package org.example.visitor;

import org.example.metrics.MetricsContext;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MetricsMethodVisitor extends MethodVisitor {

    private final MetricsContext metricsContext;

    public MetricsMethodVisitor(MetricsContext metricsContext, MethodVisitor methodVisitor) {
        super(Opcodes.ASM9, methodVisitor);
        this.metricsContext = metricsContext;
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        switch (opcode) {
            case Opcodes.ISTORE:
            case Opcodes.LSTORE:
            case Opcodes.FSTORE:
            case Opcodes.DSTORE:
            case Opcodes.ASTORE:
                metricsContext.incrementAssignments();
                break;
        }
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        metricsContext.incrementAssignments();
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        metricsContext.incrementBranches();
        if (isConditionalJump(opcode)) {
            metricsContext.incrementConditions();
        }
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        metricsContext.incrementBranches();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        metricsContext.incrementBranches();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    private boolean isConditionalJump(int opcode) {
        return switch (opcode) {
            case Opcodes.IFEQ, Opcodes.IFNE, Opcodes.IFLT, Opcodes.IFGE, Opcodes.IFGT, Opcodes.IFLE, Opcodes.IF_ICMPEQ,
                 Opcodes.IF_ICMPNE, Opcodes.IF_ICMPLT, Opcodes.IF_ICMPGE, Opcodes.IF_ICMPGT, Opcodes.IF_ICMPLE,
                 Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE, Opcodes.IFNULL, Opcodes.IFNONNULL -> true;
            default -> false;
        };
    }
}
