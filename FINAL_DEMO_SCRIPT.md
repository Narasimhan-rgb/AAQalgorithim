# Final Demo Script

## Project Title

Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform

## Proposed Algorithm

Adaptive Amplitude QuickSort - AAQ

---

# 1. Introduction

Good morning / good afternoon.

My project is titled **Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform**.

The main contribution of this project is the proposed algorithm called **Adaptive Amplitude QuickSort**, also called **AAQ**.

AAQ is a quantum-inspired sorting algorithm that improves classical QuickSort pivot selection using amplitude-weighted probability, partition imbalance estimation, constructive reinforcement, destructive suppression, and neighbor propagation.

Traditional algorithms such as Java Built-in Sort, QuickSort, MergeSort, HeapSort, and Parallel Sort are used only as baseline algorithms for validation.

---

# 2. Technology Stack

The project has three main layers.

## Backend

The backend is built using:

```text
Java 21
Spring Boot
Spring Data JPA
PostgreSQL
Maven
```

It handles:

```text
Dataset metadata
Sorting jobs
AAQ execution
Benchmark storage
Recommendation
Report generation
Dashboard APIs
```

## Python Service

The Python service is built using:

```text
FastAPI
Polars
NumPy
SciPy
Qiskit
```

It handles:

```text
Dataset profiling
Pattern detection
Amplitude simulation
Interference simulation
OpenQASM generation
Qiskit circuit generation
```

## Frontend

The frontend is built using:

```text
React
Vite
Axios
React Router
CSS
```

It provides:

```text
Dashboard
Dataset Management
Dataset Details
Quantum Analyzer
Benchmark Results
Recommendation and Reports
System Status
```

---

# 3. How to Run the Project

## Start Backend

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\paper3"
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

## Start Python Service

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\python-service"
uvicorn main:app --reload --port 8000
```

Python service runs on:

```text
http://localhost:8000
```

## Start Frontend

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\aaq-frontend"
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

# 4. Demo Flow

## Step 1: Dashboard

First, I open the dashboard.

The dashboard shows:

```text
Total datasets
Sorting jobs
Completed jobs
Benchmark results
Recommendations
Quantum metrics
Python simulation status
Best AAQ time
Average throughput
```

This confirms that the backend, database, benchmark module, dashboard module, and Python simulation status are working.

---

## Step 2: Dataset Management

Next, I open the dataset management page.

Here, the user can upload a dataset, select the sorting column, view uploaded datasets, analyze datasets, open quantum analysis, run benchmarks, and generate reports.

The uploaded dataset contains metadata such as:

```text
Dataset name
File type
File size
Row count
Column count
Selected sort column
Detected pattern
Created time
```

---

## Step 3: Dataset Details and Preview

Then, I open the dataset details page.

This page shows:

```text
Dataset metadata
Selected column
Data type
Detected pattern
Null percentage
Duplicate percentage
Skewness
Sortedness score
Preview records
```

The preview proves that the uploaded dataset is correctly read and displayed.

---

## Step 4: Python Dataset Analysis

Next, I click Analyze Dataset.

The Java backend sends the dataset path and selected column to the Python FastAPI service.

The Python service uses Polars to calculate:

```text
Row count
Column count
Null percentage
Duplicate percentage
Minimum value
Maximum value
Mean
Median
Standard deviation
Skewness
Sortedness score
Detected pattern
```

The result is stored in PostgreSQL and displayed in the frontend.

---

## Step 5: Quantum Analyzer

Next, I open the Quantum Analyzer page.

This page explains the quantum-inspired behavior of AAQ.

It shows:

```text
Amplitude probability distribution
Selected pivot candidate
Partition imbalance
Amplitude weights
Interference update
OpenQASM circuit
Qiskit circuit
```

This is used to visually explain how AAQ selects better pivots.

---

## Step 6: Amplitude Simulation

In amplitude simulation, multiple pivot candidates are selected.

Each candidate receives a probability weight.

A better pivot candidate receives a higher amplitude probability.

This proves that AAQ does not select pivots blindly. It evaluates pivot quality before selecting.

---

## Step 7: Interference Simulation

In interference simulation, AAQ applies:

```text
Constructive reinforcement
Destructive suppression
```

Good pivot regions are strengthened.

Poor pivot regions are weakened.

This improves future pivot selection.

---

## Step 8: OpenQASM and Qiskit

The system can generate OpenQASM and Qiskit circuit representations.

These are not used for real quantum hardware execution.

They are used for research explanation and visualization.

The project is quantum-inspired, not real quantum hardware based.

---

## Step 9: Benchmark Results

Next, I open benchmark results.

This page compares AAQ with baseline algorithms:

```text
Java Built-in Sort
QuickSort
MergeSort
HeapSort
Parallel Sort
Adaptive Amplitude QuickSort
```

The benchmark compares:

```text
Execution time
Throughput
Memory usage
Partition imbalance
Improvement percentage
Success status
```

If Java Built-in Sort is faster, that is expected because it is highly optimized in the JVM.

AAQ is the proposed research algorithm, mainly designed for difficult datasets such as skewed, repeated, and adversarial data.

---

## Step 10: Recommendation and Reports

Next, I open the recommendation and reports page.

The recommendation engine uses:

```text
Dataset pattern
Duplicate percentage
Skewness
Benchmark results
AAQ metrics
```

It recommends the suitable algorithm and explains the reason.

The report module generates a final report containing dataset details, analysis, benchmark result, recommendation, and conclusion.

---

## Step 11: System Status

Finally, I open the system status page.

This page confirms:

```text
Backend running
Database connected
Python service connected
System ready
```

This proves that the complete system is working end-to-end.

---

# 5. Important Explanation Points

## Users Table

The users table is available in the backend for authentication and future role-based tracking.

In the current demo, strict frontend login enforcement is relaxed.

So fields like:

```text
createdBy = N/A
uploadedBy = N/A
```

may appear.

This is documented in known limitations.

## N/A Values

N/A means the value is null, optional, or not captured for that record.

It is not a project failure.

## Java Built-in Sort Faster Than AAQ

Java Built-in Sort may be faster because it is highly optimized.

AAQ is not designed to beat every algorithm on every dataset.

AAQ focuses on improving pivot behavior and partition balance on difficult dataset patterns.

---

# 6. Final Conclusion

This project successfully implements a complete quantum-inspired sorting optimization platform.

It includes:

```text
Dataset upload
Dataset profiling
Quantum-inspired analysis
Adaptive Amplitude QuickSort
Baseline benchmarking
Recommendation engine
Report generation
Dashboard visualization
System status monitoring
```

The main contribution is the proposed **Adaptive Amplitude QuickSort** algorithm and the complete enterprise-level platform built around it.

Thank you.  