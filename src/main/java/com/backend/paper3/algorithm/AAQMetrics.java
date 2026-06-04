package com.backend.paper3.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AAQMetrics {

    private Long comparisonCount = 0L;

    private Long swapCount = 0L;

    private Long pivotSelectionCount = 0L;

    private Long insertionSortUsageCount = 0L;

    private Long heapFallbackCount = 0L;

    private Long partitionCount = 0L;

    private Double averagePartitionImbalance = 0.0;

    private Double maxPartitionImbalance = 0.0;

    public void incrementComparison() {
        comparisonCount++;
    }

    public void incrementSwap() {
        swapCount++;
    }

    public void incrementPivotSelection() {
        pivotSelectionCount++;
    }

    public void incrementInsertionSortUsage() {
        insertionSortUsageCount++;
    }

    public void incrementHeapFallback() {
        heapFallbackCount++;
    }

    public void recordPartitionImbalance(
            double imbalance
    ) {

        partitionCount++;

        double previousTotal =
                averagePartitionImbalance * (partitionCount - 1);

        averagePartitionImbalance =
                (previousTotal + imbalance) / partitionCount;

        if (imbalance > maxPartitionImbalance) {
            maxPartitionImbalance = imbalance;
        }
    }
}