# Adaptive Amplitude QuickSort (AAQ)

> A classical quantum-inspired sorting research project focused on adaptive pivot selection, dataset-pattern analysis, benchmarking, and reproducible algorithm evaluation.

## Important scope note

AAQ is a **classical quantum-inspired algorithm**. It does not claim real quantum speedup and does not require quantum hardware for the benchmark results. Quantum terminology is used as an inspiration for adaptive probability weighting and amplitude-style pivot-region selection.

## Research objective

The goal of this project is to study whether adaptive probability-guided pivot selection can reduce partition imbalance in difficult sorting cases such as:

- skewed datasets
- repeated-value datasets
- nearly sorted datasets
- adversarial datasets
- structured real-world datasets

Traditional sorting algorithms are used as validation baselines.

## System overview

The full platform is designed as a research and benchmarking system:

```text
Dataset upload
→ dataset profiling
→ pattern detection
→ AAQ sorting/benchmarking
→ baseline comparison
→ metrics and recommendations
→ dashboard/report generation
```

## Core contribution

Adaptive Amplitude QuickSort adjusts pivot-region selection probabilities based on observed partition behaviour. The research question is whether this adaptive strategy improves practical behaviour on non-uniform datasets while preserving the expected classical sorting complexity.

## Technology stack

| Module | Technology |
|---|---|
| Backend platform | Java 21, Spring Boot |
| Dataset analysis service | Python, FastAPI, Polars |
| Database | PostgreSQL |
| Frontend | React, Vite |
| Benchmarking | Classical baselines + AAQ metrics |

## Repository role

This repository contains the main AAQ backend/research platform. Related components:

- `AAQ_frontend` — React dashboard/frontend
- `python_services-` — Python dataset analysis and research-support service

## Suggested local setup

```cmd
git clone https://github.com/Narasimhan-rgb/AAQalgorithim.git
cd AAQalgorithim
```

Then run the backend according to the Spring Boot configuration in the project.

## MS portfolio value

This project supports my MS profile in algorithms, systems, and research engineering. It shows the ability to propose an algorithmic idea, implement a benchmarking platform, compare with baselines, and communicate limitations honestly.

## Roadmap

- Rename repository to `adaptive-amplitude-quicksort`
- Add benchmark result screenshots
- Add architecture diagram
- Add reproducible sample dataset
- Add paper citation once formally accepted or published
- Keep the quantum-inspired scope statement visible in all documentation
