package org.example.metrics;

public class ClassInfo {
    public final String name;
    public final String superName;
    public int fieldCount = 0;

    public ClassInfo(String name, String superName) {
        this.name = name;
        this.superName = superName;
    }
}
