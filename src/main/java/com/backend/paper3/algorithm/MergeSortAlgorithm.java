package com.backend.paper3.algorithm;

import java.util.ArrayList;
import java.util.List;

public class MergeSortAlgorithm
        implements SortingAlgorithm {

    private long comparisonCount;

    @Override
    public SortingAlgorithmResult sort(
            List<String> input
    ) {

        List<String> values =
                input == null
                        ? new ArrayList<>()
                        : new ArrayList<>(input);

        comparisonCount = 0L;

        long startTime =
                System.nanoTime();

        values =
                mergeSort(values);

        long endTime =
                System.nanoTime();

        SortingAlgorithmResult result =
                new SortingAlgorithmResult();

        result.setSortedValues(values);
        result.setExecutionTimeMs(
                (endTime - startTime) / 1_000_000
        );
        result.setComparisonCount(comparisonCount);
        result.setSwapCount(0L);
        result.setInputSize((long) values.size());
        result.setAlgorithmName(getAlgorithmName());

        return result;
    }

    private List<String> mergeSort(
            List<String> values
    ) {

        if (values.size() <= 1) {
            return values;
        }

        int middle =
                values.size() / 2;

        List<String> left =
                mergeSort(
                        new ArrayList<>(
                                values.subList(
                                        0,
                                        middle
                                )
                        )
                );

        List<String> right =
                mergeSort(
                        new ArrayList<>(
                                values.subList(
                                        middle,
                                        values.size()
                                )
                        )
                );

        return merge(
                left,
                right
        );
    }

    private List<String> merge(
            List<String> left,
            List<String> right
    ) {

        List<String> result =
                new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < left.size()
                && j < right.size()) {

            comparisonCount++;

            if (left.get(i).compareTo(right.get(j)) <= 0) {

                result.add(
                        left.get(i)
                );

                i++;

            } else {

                result.add(
                        right.get(j)
                );

                j++;
            }
        }

        while (i < left.size()) {
            result.add(
                    left.get(i)
            );
            i++;
        }

        while (j < right.size()) {
            result.add(
                    right.get(j)
            );
            j++;
        }

        return result;
    }

    @Override
    public String getAlgorithmName() {
        return "MERGESORT";
    }
}