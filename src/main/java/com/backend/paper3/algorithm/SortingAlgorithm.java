package com.backend.paper3.algorithm;

import java.util.List;

public interface SortingAlgorithm {

    SortingAlgorithmResult sort(
            List<String> input
    );

    String getAlgorithmName();
}