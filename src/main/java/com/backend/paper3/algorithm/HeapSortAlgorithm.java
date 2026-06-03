package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.List;

public class HeapSortAlgorithm
        implements SortingAlgorithm {

    private long comparisonCount;

    private long swapCount;

    @Override
    public SortingAlgorithmResult sort(
            List<String> input
    ) {

        List<String> values =
                input == null
                        ? new ArrayList<>()
                        : new ArrayList<>(input);

        comparisonCount = 0L;
        swapCount = 0L;

        long startTime =
                System.nanoTime();

        heapSort(values);

        long endTime =
                System.nanoTime();

        SortingAlgorithmResult result =
                new SortingAlgorithmResult();

        result.setSortedValues(values);
        result.setExecutionTimeMs(
                (endTime - startTime) / 1_000_000
        );
        result.setComparisonCount(comparisonCount);
        result.setSwapCount(swapCount);
        result.setInputSize((long) values.size());
        result.setAlgorithmName(getAlgorithmName());

        return result;
    }

    private void heapSort(
            List<String> values
    ) {

        int n =
                values.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(
                    values,
                    n,
                    i
            );
        }

        for (int i = n - 1; i > 0; i--) {

            swap(
                    values,
                    0,
                    i
            );

            heapify(
                    values,
                    i,
                    0
            );
        }
    }

    private void heapify(
            List<String> values,
            int n,
            int i
    ) {

        int largest =
                i;

        int left =
                2 * i + 1;

        int right =
                2 * i + 2;

        if (left < n) {

            comparisonCount++;

            if (values.get(left).compareTo(values.get(largest)) > 0) {
                largest = left;
            }
        }

        if (right < n) {

            comparisonCount++;

            if (values.get(right).compareTo(values.get(largest)) > 0) {
                largest = right;
            }
        }

        if (largest != i) {

            swap(
                    values,
                    i,
                    largest
            );

            heapify(
                    values,
                    n,
                    largest
            );
        }
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

        swapCount++;
    }

    @Override
    public String getAlgorithmName() {
        return "HEAPSORT";
    }
}