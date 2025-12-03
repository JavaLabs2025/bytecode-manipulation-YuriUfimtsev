package org.example.metrics.model;

import lombok.Getter;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassInfo {
    @Getter
    private final String name;

    @Getter
    private final String superName;

    @Getter
    private final boolean isInterface;

    @Getter
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

    public List<MethodInfo> getAllMethodsInfo() {
        return Collections.unmodifiableList(methods);
    }

    public void incrementFieldCount() {
        ++fieldCount;
    }
}
