# Adaptive Amplitude QuickSort (AAQ)

> Canonical repository for the AAQ research platform: Java/Spring Boot backend, Adaptive Amplitude QuickSort research engine, reproducibility workflow, React dashboard, and Python analysis service.

## Scope

AAQ is a **classical quantum-inspired sorting research project**. It does not require quantum hardware and does not claim a universal quantum speedup. The project studies adaptive, probability-guided pivot selection and its behaviour across structured and difficult workloads.

## Repository layout

```text
AAQalgorithim/
├─ src/                 Java 21 / Spring Boot backend and AAQ engine
├─ reports/             curated research outputs
├─ docs/                architecture, setup, and repository notes
├─ frontend/            Git submodule → AAQ_frontend
├─ python-service/      Git submodule → python_services-
├─ pom.xml
└─ README.md
```

The former frontend and Python repositories remain separate component histories, but this repository is now the **single entry point** for the complete AAQ system.

## Clone the complete project

```bash
git clone --recurse-submodules https://github.com/Narasimhan-rgb/AAQalgorithim.git
cd AAQalgorithim
```

For an existing clone:

```bash
git submodule update --init --recursive
```

## Main components

| Component | Technology | Role |
|---|---|---|
| Backend / sorting engine | Java 21, Spring Boot | AAQ execution, APIs, persistence, reports |
| Benchmarks | JMH | controlled reproducibility experiments |
| Database | PostgreSQL | datasets, jobs, benchmark results |
| Python service | FastAPI, Polars | profiling, workload analysis, research support |
| Frontend | React, Vite, Recharts | dashboard, live metrics, benchmark views |

## Local services

Default development ports:

```text
Java backend   http://localhost:8080
Python service http://127.0.0.1:8000
Frontend       http://localhost:5173
```

### Required backend environment variables

The repository no longer stores local database passwords or JWT secrets in source control.

```text
AAQ_DB_PASSWORD=<your local PostgreSQL password>
AAQ_JWT_SECRET=<a long random secret>
```

Optional overrides include `AAQ_DB_URL`, `AAQ_DB_USERNAME`, `AAQ_DB_SCHEMA`, `AAQ_FILE_STORAGE`, and `AAQ_PYTHON_SERVICE_URL`.

See [docs/SECURITY_AND_LOCAL_SETUP.md](docs/SECURITY_AND_LOCAL_SETUP.md).

## Research workflow

```text
Dataset / controlled workload
        ↓
Python profiling and pattern analysis
        ↓
AAQ + classical baseline execution
        ↓
Live execution metrics
        ↓
JMH benchmark persistence
        ↓
Reproducibility analysis
        ↓
Dashboard and reports
```

The controlled benchmark workflow is designed to compare algorithms on the same workload, seed, and runtime conditions. Runtime application timings should not be substituted for controlled JMH measurements.

## Data

The project uses controlled synthetic workloads and public/open-source datasets for realistic evaluation. Large datasets and generated benchmark artifacts are intentionally excluded from Git.

## Repository hygiene

Do not commit:

- passwords, API keys, JWT secrets, or `.env` files
- compiled `.class` files or build directories
- local SQLite/PostgreSQL database files
- generated benchmark outputs
- large raw datasets
- IDE-specific files

## Documentation

- [Repository structure](docs/REPOSITORY_STRUCTURE.md)
- [Security and local setup](docs/SECURITY_AND_LOCAL_SETUP.md)
- [Project progress](docs/PROJECT_PROGRESS.md)
- [Known limitations](docs/KNOWN_LIMITATIONS.md)
- [Demo script](docs/DEMO_SCRIPT.md)
- [AAQ report sample](docs/examples/AAQ_REPORT_SAMPLE.txt)
