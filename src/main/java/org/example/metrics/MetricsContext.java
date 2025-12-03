package org.example.metrics;

import java.util.HashMap;
import java.util.Map;

public class MetricsContext {
    public final Map<String, ClassInfo> classesInfo = new HashMap<>();

    public ClassInfo getOrCreateClass(String name, String superName) {
        return classesInfo.computeIfAbsent(name, n -> new ClassInfo(n, superName));
    }
}
