package org.example.metrics;

import org.example.metrics.model.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
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
            return new MetricsResult(0, 0.0, 0.0);
        }

        var depthCache = new HashMap<String, Integer>();
        var maxDepth = 0;
        var sumDepth = 0;
        var sumFields = 0;

        for (var classInfo : classesInfoList) {
            var depth = calculateDepth(classInfo.getName(), depthCache);

            maxDepth = Math.max(maxDepth, depth);
            sumDepth += depth;
            sumFields += classInfo.getFieldCount();
        }

        double avgDepth = sumDepth * 1.0 / classesInfoList.size();
        double avgFields = sumFields * 1.0 / classesInfoList.size();

        return new MetricsResult(maxDepth, avgDepth, avgFields);
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
}
