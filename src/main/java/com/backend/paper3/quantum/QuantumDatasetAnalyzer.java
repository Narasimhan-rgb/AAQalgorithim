package com.backend.paper3.quantum;

import org.springframework.stereotype.Component;

@Component
public class QuantumDatasetAnalyzer {

    public double calculateQuantumScore(Double value) {

        if (value == null || value <= 0) {
            return 0.0;
        }

        double amplitude = Math.sqrt(value);

        return amplitude * amplitude * Math.log(1 + value);
    }
}