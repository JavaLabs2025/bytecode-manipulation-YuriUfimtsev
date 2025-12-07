package org.example.metrics;

import lombok.Builder;
import lombok.Getter;

// todo: refactor to record
@Builder
@Getter
public class MetricsResult {
    private final int maxInheritanceDepth;
    private final double avgInheritanceDepth;

    private final long abcAssignments;
    private final long abcBranches;
    private final long abcConditions;

    private final double avgOverriddenMethods;
    private final double avgFieldsPerClass;
    private final int classesCount;

    @Override
    public String toString() {
        return "Classes: " + classesCount + "\n" +
                "Max inheritance depth: " + maxInheritanceDepth + "\n" +
                "Avg inheritance depth: " + avgInheritanceDepth + "\n" +
                "ABC: A=" + abcAssignments + ", B=" + abcBranches + ", C=" + abcConditions + "\n" +
                "Avg overridden methods per class: " + avgOverriddenMethods + "\n" +
                "Avg fields per class: " + avgFieldsPerClass + "\n";
    }
}
