package com.backend.paper3.service;

import java.util.Map;

import com.backend.paper3.dto.PythonQuantumRequestDto;

public interface PythonQuantumClient {

    Map<String, Object> simulateAmplitude(
            PythonQuantumRequestDto request
    );

    Map<String, Object> simulateInterference(
            PythonQuantumRequestDto request
    );

    Map<String, Object> generateQasm(
            PythonQuantumRequestDto request
    );

    Map<String, Object> generateQiskitCircuit(
            PythonQuantumRequestDto request
    );
}