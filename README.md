# Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform

## Proposed Algorithm

# Adaptive Amplitude QuickSort - AAQ

---

## 1. Project Overview

This project is an enterprise-level **Quantum-Inspired Sorting Optimization Platform** built around the proposed algorithm:

```text
Adaptive Amplitude QuickSort - AAQ
```

AAQ is a quantum-inspired sorting algorithm designed to improve pivot selection and reduce partition imbalance on difficult datasets such as:

```text
Skewed datasets
Repeated-value datasets
Nearly sorted datasets
Adversarial datasets
Structured real-world datasets
```

Traditional sorting algorithms are used only as validation baselines. The main contribution of this project is the proposed AAQ algorithm and the complete benchmarking, analysis, recommendation, and reporting platform built around it.

---

## 2. Main Objective

The main objective of this project is to design and implement a Java 21 and Python Polars based performance optimization platform that:

```text
Uploads and manages datasets
Profiles datasets using Python Polars
Detects dataset patterns
Runs Adaptive Amplitude QuickSort
Compares AAQ with traditional baselines
Stores benchmark and quantum-inspired metrics
Generates algorithm recommendations
Generates reports
Displays dashboard analytics
```

---

## 3. Why This Project Is Needed

Classical QuickSort depends heavily on pivot quality.

A good pivot creates balanced partitions:

```text
left side ≈ right side
```

A bad pivot creates imbalanced partitions:

```text
one side very large
one side very small
```

Poor pivot selection can cause:

```text
High recursion depth
More comparisons
More swaps
Longer execution time
Poor behavior on skewed or adversarial data
```

AAQ solves this by learning which pivot regions are better and increasing their probability of selection.

---

## 4. Core Idea of AAQ

AAQ improves classical QuickSort using quantum-inspired concepts:

| Quantum-Inspired Concept | AAQ Meaning |
|---|---|
| Superposition-inspired candidate space | Multiple pivot candidates are considered |
| Amplitude weight | Candidate pivot probability |
| Amplitude amplification | Good pivot regions are strengthened |
| Interference | Good choices are reinforced, poor choices are suppressed |
| Entanglement-inspired neighbor correlation | Nearby pivot regions are updated together |
| Heap fallback | Worst-case protection |

---

## 5. AAQ Algorithm Features

AAQ includes:

```text
Amplitude-weighted pivot sampling
Candidate pivot scoring
Partition balance estimation
Constructive reinforcement
Destructive suppression
Neighbor propagation
Amplitude convergence tracking
HeapSort fallback protection
Quantum metric collection
```

---

## 6. Technology Stack

## Backend

```text
Java 21
Spring Boot
Spring Data JPA
PostgreSQL
Maven
Lombok
MapStruct
Swagger/OpenAPI
```

## Python Service

```text
Python
FastAPI
Polars
NumPy
SciPy
Qiskit
Uvicorn
```

## Frontend

```text
React
Vite
Axios
React Router
CSS
```

## Database

```text
PostgreSQL
```

---

## 7. System Architecture

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
        | Calls dataset profiling and quantum simulation
        v
Python FastAPI + Polars Service
```

---

## 8. Main Modules

## 8.1 Dataset Management

```text
Upload dataset
Store dataset metadata
List datasets
View dataset details
Preview dataset rows
Delete dataset
```

## 8.2 Dataset Profiling

Using Python Polars, the system calculates:

```text
Row count
Column count
Data type
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

## 8.3 Quantum-Inspired Analysis

The platform provides quantum-inspired simulation using:

```text
Amplitude probability distribution
Pivot candidate probability
Partition imbalance curve
Interference update
OpenQASM generation
Qiskit circuit generation
```

## 8.4 Sorting and Benchmarking

The system runs:

```text
Adaptive Amplitude QuickSort
Java Built-in Sort
QuickSort
MergeSort
HeapSort
Parallel Sort
```

Metrics captured:

```text
Execution time
Throughput
Memory usage
Comparison count
Swap count
Recursion depth
Partition imbalance
Improvement percentage
Success status
```

## 8.5 Recommendation Engine

The recommendation engine uses:

```text
Detected dataset pattern
Duplicate percentage
Skewness
Sortedness score
Benchmark results
AAQ metrics
```

It recommends the most suitable algorithm and provides a reason.

## 8.6 Report Generation

The report module generates report data containing:

```text
Dataset details
Dataset profile
Benchmark results
Recommendation
Quantum-inspired metrics
Final conclusion
```

## 8.7 Dashboard

The dashboard displays:

```text
Total datasets
Sorting jobs
Completed jobs
Pending jobs
Benchmark results
Recommendations
Quantum metrics
Best AAQ time
Average throughput
Python simulation status
```

---

## 9. Database Tables

The backend stores data in PostgreSQL.

Main tables:

```text
users
datasets
sorting_jobs
benchmark_results
quantum_aaq_metrics
amplitude_profiles
algorithm_recommendations
reports
```

---

## 10. Backend API Overview

Backend base URL:

```text
http://localhost:8080/api
```

## Auth APIs

```text
POST /auth/register
POST /auth/login
POST /auth/logout
GET  /auth/profile
```

## Dataset APIs

```text
POST   /datasets/upload
GET    /datasets
GET    /datasets/{id}
DELETE /datasets/{id}
POST   /datasets/{id}/analyze
GET    /datasets/{id}/preview
```

## Benchmark APIs

```text
POST /benchmarks/run
GET  /benchmarks/job/{jobId}
GET  /benchmarks/dataset/{datasetId}
GET  /benchmarks/compare/{datasetId}
```

## Recommendation APIs

```text
GET /reports/dataset/{datasetId}/summary
```

## Report APIs

```text
POST /reports/job/{jobId}/generate
GET  /reports/job/{jobId}
GET  /reports/job/{jobId}/download
```

## Dashboard APIs

```text
GET /dashboard/summary
GET /dashboard/algorithm-performance
GET /dashboard/dataset-patterns
GET /dashboard/aaq-improvements
GET /dashboard/quantum-metrics
GET /dashboard/recent-jobs
```

---

## 11. Python Service API Overview

Python service base URL:

```text
http://localhost:8000
```

Python APIs:

```text
GET  /health
POST /profile/dataset
POST /detect/distribution
POST /quantum/amplitude-simulate
POST /quantum/interference-simulate
POST /quantum/generate-qasm
POST /quantum/qiskit-circuit
```

---

## 12. How to Run the Project

## Step 1: Start Backend

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\paper3"
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

## Step 2: Start Python Service

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\python-service"
uvicorn main:app --reload --port 8000
```

Python service runs on:

```text
http://localhost:8000
```

---

## Step 3: Start Frontend

```bash
cd "D:\AJVRPS TECH\researchpaper\papers\processing papers\Quantum-Inspired Sorting Algorithmsog\aaq-frontend"
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

## 13. Recommended Demo Flow

```text
1. Open dashboard
2. Open dataset management
3. Upload or view dataset
4. Open dataset details
5. Preview dataset rows
6. Analyze dataset using Python Polars
7. Open Quantum Analyzer
8. Run amplitude simulation
9. Run interference simulation
10. Generate OpenQASM
11. Generate Qiskit circuit
12. Open benchmark results
13. Compare AAQ with baselines
14. Open recommendation and reports
15. Generate report
16. Open system status
```

---

## 14. Important Demo Explanation

## Is this real quantum computing?

No.

This project is **quantum-inspired**, not real quantum hardware based.

It uses classical simulation of quantum-inspired ideas such as:

```text
Amplitude probability
Constructive reinforcement
Destructive suppression
Neighbor correlation
QASM/Qiskit-style circuit visualization
```

## Why can Java Built-in Sort be faster?

Java Built-in Sort is highly optimized inside the JVM.

AAQ is a proposed research algorithm. It is mainly designed to improve pivot behavior on difficult datasets such as skewed, repeated, and adversarial data.

## Why are some values N/A?

`N/A` means the value is null, optional, or not captured for that record.

Examples:

```text
createdBy = N/A
uploadedBy = N/A
memoryUsage = N/A
cpuUsage = N/A
```

This is not a project failure.

---

## 15. Known Limitations

See:

```text
KNOWN_LIMITATIONS.md
```

Important limitations:

```text
Frontend login enforcement is relaxed for demo
Some user tracking fields may show N/A
Report download is limited in current version
Benchmark results depend on local machine performance
No real quantum hardware is used
Python service must be running for analysis and simulation
```

---

## 16. Final Demo Script

See:

```text
FINAL_DEMO_SCRIPT.md
```

This file contains the short final explanation script for viva/demo.

---

## 17. Final Deliverables

```text
Working Java 21 backend
Working React frontend
Working Python FastAPI service
PostgreSQL database integration
Dataset upload and preview
Dataset profiling
Quantum Analyzer
AAQ algorithm implementation
Baseline benchmark comparison
Recommendation engine
Report generation
Dashboard visualization
System status page
Final demo script
Known limitations document
```

---

## 18. Project Conclusion

This project successfully implements a complete **Quantum-Inspired Adaptive Sorting Optimization Platform**.

The main contribution is the proposed:

```text
Adaptive Amplitude QuickSort - AAQ
```

The platform demonstrates how quantum-inspired concepts such as amplitude weighting, constructive reinforcement, destructive suppression, and neighbor correlation can be applied to classical sorting optimization.

Traditional algorithms are used only as baselines to validate AAQ.

The system combines backend APIs, Python dataset profiling, PostgreSQL persistence, React visualization, benchmarking, recommendation, and report generation into one complete enterprise-level research platform.