# AAQ Repository Structure

This repository is the canonical entry point for the Adaptive Amplitude QuickSort (AAQ) research platform.

## Components

- **Java backend / research engine (this repository root)** — Spring Boot API, AAQ implementation, benchmark orchestration, persistence, reports, and JMH workflow.
- **frontend/** — React/Vite dashboard tracked as the `AAQ_frontend` Git submodule.
- **python-service/** — FastAPI/Polars profiling and analysis service tracked as the `python_services-` Git submodule.
- **reports/** — generated or curated research outputs; generated runtime artifacts should remain ignored.
- **docs/** — architecture, setup, reproducibility, and repository-maintenance notes.

## Clone the complete project

```bash
git clone --recurse-submodules https://github.com/Narasimhan-rgb/AAQalgorithim.git
cd AAQalgorithim
```

For an existing clone:

```bash
git submodule update --init --recursive
```

## Repository hygiene

Do not commit compiled classes, local databases, secrets, IDE output, generated benchmark files, or local dataset copies. Runtime credentials must be supplied through environment variables.
