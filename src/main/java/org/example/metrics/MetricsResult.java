package org.example.metrics;


public class MetricsResult {
    private final int maxInheritanceDepth;
    private final double avgInheritanceDepth;
    private final double avgFieldsPerClass;

    public MetricsResult(int maxInheritanceDepth, double avgInheritanceDepth, double avgFieldsPerClass) {
        this.maxInheritanceDepth = maxInheritanceDepth;
        this.avgInheritanceDepth = avgInheritanceDepth;
        this.avgFieldsPerClass = avgFieldsPerClass;
    }

    @Override
    public String toString() {
        return "Max inheritance depth: " + maxInheritanceDepth +
                "\nAvg inheritance depth: " + avgInheritanceDepth +
                "\nAvg fields per class: " + avgFieldsPerClass + "\n";
    }
}
