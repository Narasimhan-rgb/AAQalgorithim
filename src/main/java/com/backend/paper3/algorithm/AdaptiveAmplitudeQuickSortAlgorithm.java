package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.List;

public class AdaptiveAmplitudeQuickSortAlgorithm
        implements SortingAlgorithm {

    private AAQConfiguration configuration;

    private AAQMetrics metrics;

    public AdaptiveAmplitudeQuickSortAlgorithm() {

        this.configuration =
                AAQConfiguration.defaultConfig();

        this.metrics =
                new AAQMetrics();
    }

    public AdaptiveAmplitudeQuickSortAlgorithm(
            AAQConfiguration configuration
    ) {

        this.configuration =
                configuration == null
                        ? AAQConfiguration.defaultConfig()
                        : configuration;

        this.metrics =
                new AAQMetrics();
    }

    @Override
    public SortingAlgorithmResult sort(
            List<String> input
    ) {

        List<String> values =
                input == null
                        ? new ArrayList<>()
                        : new ArrayList<>(input);

        this.metrics =
                new AAQMetrics();

        long startTime =
                System.nanoTime();

        int maxDepth =
                calculateMaxDepth(
                        values.size()
                );

        adaptiveQuickSort(
                values,
                0,
                values.size() - 1,
                maxDepth
        );

        long endTime =
                System.nanoTime();

        SortingAlgorithmResult result =
                new SortingAlgorithmResult();

        result.setSortedValues(
                values
        );

        result.setExecutionTimeMs(
                (endTime - startTime) / 1_000_000
        );

        result.setComparisonCount(
                metrics.getComparisonCount()
        );

        result.setSwapCount(
                metrics.getSwapCount()
        );

        result.setInputSize(
                (long) values.size()
        );

        result.setAlgorithmName(
                getAlgorithmName()
        );
        result.setAaqMetrics(
                metrics
        );

        return result;
    }

    public AAQExecutionResult executeWithMetrics(
            List<String> input
    ) {

        SortingAlgorithmResult sortingResult =
                sort(input);

        AAQExecutionResult result =
                new AAQExecutionResult();

        result.setSortedValues(
                sortingResult.getSortedValues()
        );

        result.setExecutionTimeMs(
                sortingResult.getExecutionTimeMs()
        );

        result.setInputSize(
                sortingResult.getInputSize()
        );

        result.setMetrics(
                metrics
        );

        return result;
    }

    private void adaptiveQuickSort(
            List<String> values,
            int low,
            int high,
            int depthLimit
    ) {

        if (low >= high) {
            return;
        }

        int size =
                high - low + 1;

        if (size <= configuration.getInsertionSortCutoff()) {

            metrics.incrementInsertionSortUsage();

            insertionSort(
                    values,
                    low,
                    high
            );

            return;
        }

        if (depthLimit <= 0
                && configuration.isHeapSortFallbackEnabled()) {

            metrics.incrementHeapFallback();

            heapSortRange(
                    values,
                    low,
                    high
            );

            return;
        }

        int pivotIndex =
                selectAmplitudeWeightedPivot(
                        values,
                        low,
                        high
                );

        swap(
                values,
                pivotIndex,
                high
        );

        int partitionIndex =
                partition(
                        values,
                        low,
                        high
                );

        recordPartitionImbalance(
                low,
                high,
                partitionIndex
        );

        adaptiveQuickSort(
                values,
                low,
                partitionIndex - 1,
                depthLimit - 1
        );

        adaptiveQuickSort(
                values,
                partitionIndex + 1,
                high,
                depthLimit - 1
        );
    }

    private int selectAmplitudeWeightedPivot(
            List<String> values,
            int low,
            int high
    ) {

        metrics.incrementPivotSelection();

        int size =
                high - low + 1;

        if (size <= 2) {
            return low;
        }

        int sampleSize =
                Math.min(
                        configuration.getPivotSampleSize(),
                        size
                );

        int step =
                Math.max(
                        1,
                        size / sampleSize
                );

        List<Integer> candidateIndexes =
                new ArrayList<>();

        for (int index = low;
             index <= high && candidateIndexes.size() < sampleSize;
             index += step) {

            candidateIndexes.add(index);
        }

        if (!candidateIndexes.contains(high)) {
            candidateIndexes.add(high);
        }

        return chooseMedianCandidate(
                values,
                candidateIndexes
        );
    }

    private int chooseMedianCandidate(
            List<String> values,
            List<Integer> candidateIndexes
    ) {

        candidateIndexes.sort(
                (firstIndex, secondIndex) -> {
                    metrics.incrementComparison();

                    return values
                            .get(firstIndex)
                            .compareTo(
                                    values.get(secondIndex)
                            );
                }
        );

        return candidateIndexes.get(
                candidateIndexes.size() / 2
        );
    }

    private int partition(
            List<String> values,
            int low,
            int high
    ) {

        String pivot =
                values.get(high);

        int i =
                low - 1;

        for (int j = low; j < high; j++) {

            metrics.incrementComparison();

            if (values.get(j).compareTo(pivot) <= 0) {

                i++;

                swap(
                        values,
                        i,
                        j
                );
            }
        }

        swap(
                values,
                i + 1,
                high
        );

        return i + 1;
    }

    private void insertionSort(
            List<String> values,
            int low,
            int high
    ) {

        for (int i = low + 1; i <= high; i++) {

            String key =
                    values.get(i);

            int j =
                    i - 1;

            while (j >= low) {

                metrics.incrementComparison();

                if (values.get(j).compareTo(key) <= 0) {
                    break;
                }

                values.set(
                        j + 1,
                        values.get(j)
                );

                metrics.incrementSwap();

                j--;
            }

            values.set(
                    j + 1,
                    key
            );
        }
    }

    private void heapSortRange(
            List<String> values,
            int low,
            int high
    ) {

        List<String> subList =
                new ArrayList<>(
                        values.subList(
                                low,
                                high + 1
                        )
                );

        HeapSortAlgorithm heapSortAlgorithm =
                new HeapSortAlgorithm();

        SortingAlgorithmResult heapResult =
                heapSortAlgorithm.sort(subList);

        List<String> sortedSubList =
                heapResult.getSortedValues();

        for (int i = 0; i < sortedSubList.size(); i++) {

            values.set(
                    low + i,
                    sortedSubList.get(i)
            );
        }

        metrics.setComparisonCount(
                metrics.getComparisonCount()
                        + heapResult.getComparisonCount()
        );

        metrics.setSwapCount(
                metrics.getSwapCount()
                        + heapResult.getSwapCount()
        );
    }

    private void swap(
            List<String> values,
            int i,
            int j
    ) {

        if (i == j) {
            return;
        }

        String temp =
                values.get(i);

        values.set(
                i,
                values.get(j)
        );

        values.set(
                j,
                temp
        );

        metrics.incrementSwap();
    }

    private void recordPartitionImbalance(
            int low,
            int high,
            int partitionIndex
    ) {

        int leftSize =
                partitionIndex - low;

        int rightSize =
                high - partitionIndex;

        int total =
                high - low;

        if (total <= 0) {
            return;
        }

        int largerPartition =
                Math.max(
                        leftSize,
                        rightSize
                );

        double imbalance =
                largerPartition / (double) total;

        metrics.recordPartitionImbalance(
                imbalance
        );
    }

    private int calculateMaxDepth(
            int size
    ) {

        if (size <= 1) {
            return 1;
        }

        double log2 =
                Math.log(size) / Math.log(2);

        return (int) (
                configuration.getMaxRecursionDepthMultiplier()
                        * log2
        );
    }

    @Override
    public String getAlgorithmName() {

        return "ADAPTIVE_AMPLITUDE_QUICKSORT";
    }
}