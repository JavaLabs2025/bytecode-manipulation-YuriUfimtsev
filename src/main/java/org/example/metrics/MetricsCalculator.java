package org.example.metrics;

import org.example.metrics.model.ClassInfo;
import org.example.metrics.model.MethodInfo;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MetricsCalculator {

    private final MetricsContext metricsContext;

    public MetricsCalculator(MetricsContext metricsContext) {
        this.metricsContext = metricsContext;
    }

    public MetricsResult calculate() {
        var allClasses = metricsContext.getAllClassesInfo();

        var classesInfoList = new ArrayList<ClassInfo>();
        for (var classInfo : allClasses) {
            if (!classInfo.isInterface()) {
                classesInfoList.add(classInfo);
            }
        }

        if (classesInfoList.isEmpty()) {
            return MetricsResult.builder()
                    .maxInheritanceDepth(0)
                    .avgInheritanceDepth(0.0)
                    .abcAssignments(0L)
                    .abcBranches(0L)
                    .abcConditions(0L)
                    .avgOverriddenMethods(0.0)
                    .avgFieldsPerClass(0.0)
                    .classesCount(0)
                    .build();
        }

        var depthCache = new HashMap<String, Integer>();
        var maxDepth = 0;
        var sumDepth = 0;
        var sumFields = 0;
        var sumOverriddenMethods = 0;

        for (var classInfo : classesInfoList) {
            var depth = calculateDepth(classInfo.getName(), depthCache);
            maxDepth = Math.max(maxDepth, depth);
            sumDepth += depth;

            sumOverriddenMethods += countOverriddenMethods(classInfo);
            sumFields += classInfo.getFieldCount();
        }

        double avgDepth = sumDepth * 1.0 / classesInfoList.size();
        double avgFields = sumFields * 1.0 / classesInfoList.size();
        double avgOverriddenMethods = sumOverriddenMethods * 1.0 / classesInfoList.size();

        return MetricsResult.builder()
                .maxInheritanceDepth(maxDepth)
                .avgInheritanceDepth(avgDepth)
                .abcAssignments(metricsContext.getAssignments())
                .abcBranches(metricsContext.getBranches())
                .abcConditions(metricsContext.getConditions())
                .avgOverriddenMethods(avgOverriddenMethods)
                .avgFieldsPerClass(avgFields)
                .classesCount(classesInfoList.size())
                .build();
    }

    private int calculateDepth(String className, Map<String, Integer> depthCache) {
        var cachedDepth = depthCache.get(className);
        if (cachedDepth != null) {
            return cachedDepth;
        }

        var classInfo = metricsContext.getClassInfo(className);
        if (classInfo == null || classInfo.getSuperName() == null
                || "java/lang/Object".equals(classInfo.getSuperName())) {

            depthCache.put(className, 0);
            return 0;
        }

        var depth = 1 + calculateDepth(classInfo.getSuperName(), depthCache);
        depthCache.put(className, depth);
        return depth;
    }

    private int countOverriddenMethods(ClassInfo classInfo) {
        if (classInfo.getSuperName() == null) {
            return 0;
        }

        var superSignatures = new HashSet<String>();
        String currentSuperName = classInfo.getSuperName();

        while (currentSuperName != null) {
            ClassInfo superClassInfo = metricsContext.getClassInfo(currentSuperName);
            if (superClassInfo == null) {
                break;
            }

            for (var methodInfo : superClassInfo.getAllMethodsInfo()) {
                if (isOverridable(methodInfo)) {
                    superSignatures.add(methodInfo.signature());
                }
            }
            currentSuperName = superClassInfo.getSuperName();
        }

        var overridenMethodsCount = 0;
        for (var methodInfo : classInfo.getAllMethodsInfo()) {
            if (!isOverridable(methodInfo)) {
                continue;
            }

            if (superSignatures.contains(methodInfo.signature())) {
                ++overridenMethodsCount;
            }
        }
        return overridenMethodsCount;
    }

    private boolean isOverridable(MethodInfo methodInfo) {
        if ("<init>".equals(methodInfo.getName()) || "<clinit>".equals(methodInfo.getName())) {
            return false;
        }

        var access = methodInfo.getAccess();
        return ((access & Opcodes.ACC_STATIC) == 0) && ((access & Opcodes.ACC_PRIVATE) == 0);
    }
}
