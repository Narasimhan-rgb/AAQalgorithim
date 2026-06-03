package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaBuiltInSortAlgorithm
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

        Collections.sort(values);

        long endTime =
                System.nanoTime();

        SortingAlgorithmResult result =
                new SortingAlgorithmResult();

        result.setSortedValues(values);
        result.setExecutionTimeMs(
                (endTime - startTime) / 1_000_000
        );
        result.setComparisonCount(0L);
        result.setSwapCount(0L);
        result.setInputSize((long) values.size());
        result.setAlgorithmName(getAlgorithmName());

        return result;
    }

    @Override
    public String getAlgorithmName() {
        return "JAVA_BUILT_IN_SORT";
    }
}