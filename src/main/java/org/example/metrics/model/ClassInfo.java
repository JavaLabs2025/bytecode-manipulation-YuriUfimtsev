package org.example.metrics.model;

import lombok.Getter;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassInfo {
    private final String name;
    private final String superName;
    private final boolean isInterface;

    private int fieldCount = 0;
    private final List<MethodInfo> methods = new ArrayList<>();

    public ClassInfo(String name, String superName, int access) {
        this.name = name;
        this.superName = superName;
        this.isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
    }

    public void addMethodInfo(MethodInfo methodInfo) {
        methods.add(methodInfo);
    }

    public void incrementFieldCount() {
        ++fieldCount;
    }
}
