# AAQ Integrated Project Progress

_Last updated: 2026-08-16_

This file records the work completed across the three AAQ repositories and the integrated reproducibility build.

## Backend / algorithm work completed

- Java 21 Spring Boot backend runs through Maven Wrapper.
- PostgreSQL connectivity and AAQ schema integration were verified.
- Dataset metadata, upload, preview, analysis and selected sorting-column workflows are integrated.
- Sorting jobs can be created, executed, monitored and persisted.
- Adaptive Amplitude QuickSort V0.4 is the main research algorithm.
- AAQ uses amplitude-weighted candidate selection, partition-balance estimation, three-way partitioning, insertion-sort cutoff and Introsort-style Heapsort fallback.
- Traditional algorithms are retained as validation baselines rather than the main contribution.
- AAQ metrics include partition imbalance, comparisons, swaps, recursion/fallback information, amplitude updates, reinforcement and neighbor propagation.
- Live sorting metrics were added to support real-time frontend visualisation.
- Benchmark and AAQ metric results are persisted for comparison/reporting.

## JMH paper reproduction work

The research benchmark matrix is:

```text
6 algorithms × 15 distributions × 5 sizes × 30 seeds = 13,500 measurements
```

Per-algorithm JMH outputs were generated and combined into `paper-jmh.csv`.

Algorithms in the paper benchmark include:

- QuickSort
- Quantum/AAQ
- HeapSort
- IntroSort
- MergeSort
- TimSort

The paper benchmark uses controlled seeded synthetic workloads rather than relying on a single uploaded CSV. The same generated workload instance is used across competing algorithms for fair comparison.

## Paper-result integration

The integrated build contains a Paper V0.4 reproduction flow that separates:

- exact/reference paper artifacts
- live analysis generated from `paper-jmh.csv`

The Java backend proxies paper-analysis requests to the Python service and exposes paper-result/figure endpoints to the frontend.

## Live AAQ dashboard work

The integrated project now visualises:

- job status and progress
- processed/total records
- current, average, best and worst partition imbalance
- amplitude concentration/evolution
- throughput
- memory
- comparisons
- swaps
- partition count
- recursion depth
- Heapsort fallbacks
- amplitude updates
- reinforcement events
- neighbor propagation

Completed runs remain visible for inspection instead of disappearing immediately after the job finishes.

## Data used / source

The evaluation uses controlled synthetic workloads and public/open-source data.

Published dataset source:

- Kaggle: https://www.kaggle.com/datasets/narasimhandasarathy/quantum-amplititude-sort-testing-data/data

Open Library / Internet Archive catalogue data is also used as real-world source material where applicable.

## Important evaluation rule

Normal dataset-upload runs are useful for product demonstrations and real-world workload testing. Exact paper reproduction should be based on the controlled JMH benchmark outputs (`paper-jmh.csv`) because those results use fixed sizes, distributions, seeds and consistent benchmark conditions.

## Related repositories

- `Narasimhan-rgb/AAQalgorithim` — Java backend, AAQ algorithm and benchmark engine
- `Narasimhan-rgb/python_services-` — dataset profiling and paper/JMH analysis
- `Narasimhan-rgb/AAQ_frontend` — dashboard, live graphs and Paper V0.4 result presentation
