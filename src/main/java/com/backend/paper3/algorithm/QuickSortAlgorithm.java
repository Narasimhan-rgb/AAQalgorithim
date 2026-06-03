package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.List;

public class QuickSortAlgorithm
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

        quickSort(
                values,
                0,
                values.size() - 1
        );

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

    private void quickSort(
            List<String> values,
            int low,
            int high
    ) {

        if (low < high) {

            int pivotIndex =
                    partition(
                            values,
                            low,
                            high
                    );

            quickSort(
                    values,
                    low,
                    pivotIndex - 1
            );

            quickSort(
                    values,
                    pivotIndex + 1,
                    high
            );
        }
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

            comparisonCount++;

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
        return "QUICKSORT";
    }
}