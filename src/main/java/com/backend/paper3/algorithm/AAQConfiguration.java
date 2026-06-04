package com.backend.paper3.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AAQConfiguration {

    private int insertionSortCutoff = 16;

    private int maxRecursionDepthMultiplier = 2;

    private int pivotSampleSize = 5;

    private double imbalanceThreshold = 0.80;

    private boolean heapSortFallbackEnabled = true;

    public static AAQConfiguration defaultConfig() {
        return new AAQConfiguration();
    }
}