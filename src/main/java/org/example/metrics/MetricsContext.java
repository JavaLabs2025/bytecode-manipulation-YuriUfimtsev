package org.example.metrics;

import lombok.Getter;
import org.example.metrics.model.ClassInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MetricsContext {
    private final Map<String, ClassInfo> classesInfo = new HashMap<>();

    @Getter
    private long assignments = 0;
    @Getter
    private long branches = 0;
    @Getter
    private long conditions = 0;

    public ClassInfo getOrCreateClass(String name, String superName, int access) {
        return classesInfo.computeIfAbsent(name, n -> new ClassInfo(n, superName, access));
    }

    public ClassInfo getClassInfo(String name) {
        return classesInfo.get(name);
    }

    public Collection<ClassInfo> getAllClassesInfo() {
        return classesInfo.values();
    }

    public void incrementAssignments() {
        ++assignments;
    }

    public void incrementBranches() {
        ++branches;
    }

    public void incrementConditions() {
        ++conditions;
    }
}
