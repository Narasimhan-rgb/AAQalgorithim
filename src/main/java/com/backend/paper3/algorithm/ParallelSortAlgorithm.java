package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.List;

public class ParallelSortAlgorithm
        implements SortingAlgorithm {

    @Override
    public SortingAlgorithmResult sort(
            List<String> input
    ) {

        List<String> values =
                input == null
                        ? new ArrayList<>()
                        : new ArrayList<>(input);

        long startTime =
                System.nanoTime();

        List<String> sortedValues =
                values
                        .parallelStream()
                        .sorted()
                        .toList();

        long endTime =
                System.nanoTime();

        SortingAlgorithmResult result =
                new SortingAlgorithmResult();

        result.setSortedValues(sortedValues);
        result.setExecutionTimeMs(
                (endTime - startTime) / 1_000_000
        );
        result.setComparisonCount(0L);
        result.setSwapCount(0L);
        result.setInputSize((long) sortedValues.size());
        result.setAlgorithmName(getAlgorithmName());

        return result;
    }

    @Override
    public String getAlgorithmName() {
        return "PARALLEL_SORT";
    }
}