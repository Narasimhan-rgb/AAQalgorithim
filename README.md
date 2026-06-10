# Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform

## Project Overview

This project is an enterprise-level Quantum-Inspired Sorting Optimization Platform built around the proposed algorithm:

# Adaptive Amplitude QuickSort - AAQ

Adaptive Amplitude QuickSort is a quantum-inspired sorting algorithm designed to improve pivot selection and reduce partition imbalance on difficult datasets such as skewed, repeated, nearly sorted, and adversarial workloads.

Traditional sorting algorithms are used only as validation baselines. The main contribution of this project is the proposed AAQ algorithm and the complete benchmarking platform around it.

---

## Project Aim

The aim of this project is to design and implement a Java 21 and Python Polars based performance optimization platform that:

- Uploads and manages datasets
- Profiles datasets using Python Polars
- Detects dataset patterns
- Runs the proposed Adaptive Amplitude QuickSort algorithm
- Compares AAQ with traditional sorting baselines
- Stores benchmark and quantum-inspired metrics
- Provides dashboard, recommendation, and report generation features

---

## Proposed Algorithm

## Adaptive Amplitude QuickSort

AAQ improves classical QuickSort by using quantum-inspired ideas:

- Amplitude-weighted pivot sampling
- Partition balance estimation
- Constructive reinforcement for good pivots
- Destructive suppression for poor pivots
- Entanglement-inspired neighbor propagation
- HeapSort fallback for worst-case protection

The algorithm learns which pivot regions are more balanced and gives them higher probability during future pivot selection.

---

## Why Quantum-Inspired?

This project does not require quantum hardware.

The term quantum-inspired means that the algorithm uses classical implementations of concepts inspired by quantum computation:

| Quantum Concept | AAQ Interpretation |
|---|---|
| Superposition | Multiple pivot candidates are considered |
| Amplitude | Candidate pivot weight/probability |
| Amplitude amplification | Good pivot regions are reinforced |
| Interference | Good choices are strengthened and poor choices are suppressed |
| Entanglement-inspired correlation | Nearby pivot regions are updated together |

---

## Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- MapStruct
- Swagger/OpenAPI

## Frontend

- React
- Vite
- Axios
- React Router
- CSS-based dashboard UI

## Python Service

- Python
- FastAPI
- Polars
- NumPy
- SciPy
- Qiskit
- Uvicorn

## Database

- PostgreSQL

---

## System Architecture

```text
React Frontend
        |
        | REST API
        v
Java 21 Spring Boot Backend
        |
        | Stores metadata, jobs, benchmarks, reports
        v
PostgreSQL Database
        |
        | Calls dataset profiling service
        v
Python FastAPI + Polars Service
