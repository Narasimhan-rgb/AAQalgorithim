# Adaptive Amplitude QuickSort (AAQ)

> A classical quantum-inspired sorting research project focused on adaptive pivot selection, workload-aware benchmarking, live execution metrics, and reproducible algorithm evaluation.

## Important scope note

AAQ is a **classical quantum-inspired algorithm**. It does not require quantum hardware and does not claim a universal quantum speedup. Quantum terminology is used as an algorithm-engineering analogy for adaptive probability weighting, amplitude-style pivot-region selection, reinforcement/suppression, and correlated neighbor updates.

## Research objective

The project studies whether adaptive probability-guided pivot selection can reduce partition imbalance and improve practical sorting behaviour on difficult workloads such as:

- skewed datasets
- repeated-value datasets
- nearly sorted datasets
- reverse-sorted datasets
- adversarial or structured inputs
- recurring workloads with similar distributions

Traditional sorting algorithms are used as validation baselines.

## Current integrated project status

The integrated AAQ reproducibility build now includes:

- Java 21 Spring Boot backend
- PostgreSQL dataset/job/benchmark storage
- dataset upload, preview, metadata and selected-column handling
- Python-assisted dataset analysis and pattern detection
- Adaptive Amplitude QuickSort (AAQ) V0.4 implementation
- three-way partitioning, insertion-sort cutoff and Heapsort fallback
- amplitude-weighted pivot sampling and adaptive reinforcement/suppression
- baseline algorithms for validation
- sorting-job execution and benchmark persistence
- live AAQ execution metrics for progress, partition imbalance, throughput, memory, comparisons and swaps
- JMH paper benchmark workflow
- Paper V0.4 reference/live reproduction API integration
- report and recommendation workflow

## Paper benchmark / reproducibility

The controlled JMH experiment uses:

```text
6 algorithms
15 workload distributions
5 input sizes
30 independent seeds
= 13,500 measured benchmark rows
```

Input sizes:

```text
1,000
10,000
100,000
500,000
1,000,000
```

The combined benchmark result file is `paper-jmh.csv`. It is benchmark output and should be analyzed through the paper-reproduction workflow rather than treated as a normal sortable input dataset.

## Data used / source

The research uses a combination of controlled synthetic workloads and publicly available/open-source data for realistic evaluation.

**Published dataset source:**

- Kaggle — [Quantum Amplititude Sort Testing Data](https://www.kaggle.com/datasets/narasimhandasarathy/quantum-amplititude-sort-testing-data/data)

The synthetic workloads represent different sorting behaviours such as uniform-random, Gaussian/skewed, Zipf-like, nearly sorted, reverse sorted, organ-pipe, repeated-value, bounded-integer, streaming-chunk, high-entropy and other structured distributions. The same seeded workload instance is supplied to competing algorithms for fair comparison.

Open Library / Internet Archive catalogue records are also used as real-world source material where applicable. Dataset licensing and collection-specific usage terms should be checked before redistribution.

## System overview

```text
Dataset upload / controlled workload
        ↓
Dataset profiling and pattern detection
        ↓
AAQ execution
        ↓
Live partition/amplitude metrics
        ↓
Classical baseline validation
        ↓
Benchmark persistence
        ↓
Paper/JMH reproduction analysis
        ↓
Dashboard, recommendation and reports
```

## Core contribution

Adaptive Amplitude QuickSort maintains normalized non-negative weights over pivot regions. Candidate pivots are sampled and evaluated according to partition quality; better regions are reinforced while poor regions are suppressed. Neighboring regions can also receive correlated updates. The design keeps the sorting engine classical while using quantum-inspired probability concepts as a decision mechanism.

## Technology stack

| Module | Technology |
|---|---|
| Backend / sorting engine | Java 21, Spring Boot 4 |
| Benchmarking | JMH |
| Database | PostgreSQL |
| Dataset-analysis service | Python, FastAPI, Polars |
| Frontend | React, Vite, Recharts |

## Repository role

This repository contains the main AAQ Java backend and research/benchmarking engine.

Related repositories:

- `AAQ_frontend` — React dashboard, live AAQ graphs and Paper V0.4 results UI
- `python_services-` — Python dataset profiling and paper-reproduction analysis service

## Local backend setup

```cmd
git clone https://github.com/Narasimhan-rgb/AAQalgorithim.git
cd AAQalgorithim
.\mvnw.cmd spring-boot:run
```

The backend expects PostgreSQL configuration through the project properties/environment variables and communicates with the Python service on port `8000` by default.

## Research interpretation

The project evaluates **workload-dependent engineering advantages**, not an asymptotic quantum speedup. Paper comparisons should use controlled JMH results generated under the same workload, seed and runtime conditions for each competing algorithm.

## Progress documentation

See [`PROJECT_PROGRESS.md`](PROJECT_PROGRESS.md) for the current cross-repository implementation status and recent reproducibility work.
