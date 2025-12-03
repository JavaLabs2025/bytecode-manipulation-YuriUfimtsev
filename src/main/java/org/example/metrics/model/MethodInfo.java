package org.example.metrics.model;

import lombok.Getter;

// todo: refactor to record
@Getter
public class MethodInfo {
    private final String name;
    private final String description;
    private final int access;

    public MethodInfo(String name, String description, int access) {
        this.name = name;
        this.description = description;
        this.access = access;
    }

    public String signature() {
        return name + description;
    }
}
