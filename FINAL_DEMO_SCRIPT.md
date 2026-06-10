# Final Demo Script

## Project Title

Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform

## Main Proposed Algorithm

Adaptive Amplitude QuickSort - AAQ

---

# 1. Opening Introduction

Good morning / good afternoon.

My project is titled **Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform**.

The main contribution of this project is a proposed algorithm called **Adaptive Amplitude QuickSort**, also called **AAQ**.

AAQ is a quantum-inspired sorting algorithm that improves classical QuickSort by using amplitude-weighted pivot selection, partition imbalance estimation, constructive reinforcement, destructive suppression, and heap fallback protection.

Traditional sorting algorithms such as Java Built-in Sort, QuickSort, MergeSort, HeapSort, and Parallel Sort are used only as baseline algorithms for comparison.

The goal of the project is not only to sort data, but also to analyze dataset behavior, run quantum-inspired simulations, benchmark algorithms, recommend the best algorithm, and generate reports.

---

# 2. Technology Stack Explanation

The project is built using three major layers:

## Backend

The backend is developed using:

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

The backend handles:

- Dataset metadata
- Sorting jobs
- AAQ execution
- Baseline sorting execution
- Benchmark result storage
- Recommendation generation
- Report generation

## Python Service

The Python service is developed using:

- FastAPI
- Polars
- NumPy
- SciPy
- Qiskit

The Python service handles:

- Dataset profiling
- Pattern detection
- Quantum-inspired amplitude simulation
- Interference simulation
- OpenQASM generation
- Qiskit circuit generation

## Frontend

The frontend is developed using:

- React
- Vite
- Axios
- React Router
- CSS

The frontend provides:

- Dashboard
- Dataset management
- Dataset details and preview
- Quantum Analyzer
- Benchmark results
- Recommendation and reports
- System status page

---

# 3. Start the Project

## Step 1: Start Backend

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\paper3"
mvn spring-boot:run