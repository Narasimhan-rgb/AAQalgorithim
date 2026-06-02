
package com.backend.paper3.algorithm;

import com.backend.paper3.dto.DatasetDto;

public class DatasetScoreAlgorithm {

    // =========================================
    // SORTEDNESS SCORE
    // =========================================

    public static double calculateSortedness(
            DatasetDto dto
    ) {

        if (dto == null || dto.getValue() == null) {
            return 0.0;
        }

        double value = dto.getValue();

        return value * 0.5;
    }

    // =========================================
    // FINAL SCORE
    // =========================================

    public static double calculateFinalScore(
            double value,
            double quantumScore
    ) {

        return (value * 0.7)
                + (quantumScore * 0.3);
    }
}

