package com.backend.paper3.algorithm;

import java.util.List;

import com.backend.paper3.enums.SortingAlgorithmType;
import com.backend.paper3.exception.ApiException;

public class SortingEngine {

    public SortingAlgorithmResult execute(
            List<String> values,
            String requestedAlgorithm
    ) {

        SortingAlgorithm algorithm =
                selectAlgorithm(requestedAlgorithm);

        return algorithm.sort(values);
    }

    private SortingAlgorithm selectAlgorithm(
            String requestedAlgorithm
    ) {

        if (requestedAlgorithm == null
                || requestedAlgorithm.trim().isEmpty()) {

            return new JavaBuiltInSortAlgorithm();
        }

        String algorithmName =
                requestedAlgorithm
                        .trim()
                        .toUpperCase();

        if (algorithmName.equals(
                SortingAlgorithmType.JAVA_BUILT_IN_SORT.name()
        )) {
            return new JavaBuiltInSortAlgorithm();
        }

        if (algorithmName.equals(
                SortingAlgorithmType.QUICKSORT.name()
        )) {
            return new QuickSortAlgorithm();
        }

        if (algorithmName.equals(
                SortingAlgorithmType.MERGESORT.name()
        )) {
            return new MergeSortAlgorithm();
        }

        if (algorithmName.equals(
                SortingAlgorithmType.HEAPSORT.name()
        )) {
            return new HeapSortAlgorithm();
        }

        if (algorithmName.equals(
                SortingAlgorithmType.PARALLEL_SORT.name()
        )) {
            return new ParallelSortAlgorithm();
        }

        if (algorithmName.equals(
                SortingAlgorithmType.ADAPTIVE_AMPLITUDE_QUICKSORT.name()
        )) {
            return new AdaptiveAmplitudeQuickSortAlgorithm();
        }

        throw new ApiException(
                "Invalid sorting algorithm : " + requestedAlgorithm
        );
    }
}