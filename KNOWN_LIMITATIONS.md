# Known Limitations

## Project Title

Enterprise Quantum-Inspired Adaptive Sorting Optimization Platform

## Proposed Algorithm

Adaptive Amplitude QuickSort - AAQ

---

# 1. Frontend Authentication Limitation

The backend contains the `users` table and authentication-related backend structure.

However, in the current demo version, strict frontend login and register flow is not enforced.

Because of this, some fields may show:

```text
createdBy = N/A
uploadedBy = N/A
```

This is not a project failure. It means the current demo is configured for direct testing of dataset upload, analysis, quantum simulation, benchmarks, recommendations, and reports without forcing login.

## Future Improvement

Add complete frontend authentication:

```text
Login page
Register page
JWT token storage
Protected routes
Navbar user profile
Logout
Role-based UI control
```

---

# 2. Report Download Limitation

The backend currently supports report generation using:

```text
POST /reports/job/{jobId}/generate
```

The frontend can generate a report and show generated report metadata.

However, full report view/download APIs are not fully implemented in the current version.

So these features are limited:

```text
View old reports
Download report directly from frontend
List all reports by job
```

## Future Improvement

Add:

```text
GET /reports/job/{jobId}
GET /reports/job/{jobId}/download
GET /reports/dataset/{datasetId}
```

---

# 3. N/A Values in UI

Some UI fields may display:

```text
N/A
```

This is not an error.

It means the backend returned `null`, empty, or unavailable data for that field.

Common examples:

```text
Memory usage = N/A
CPU usage = N/A
Created By = N/A
Uploaded By = N/A
Improvement Percentage = N/A
```

## Reason

Some older benchmark records were generated before memory and CPU tracking was added.

Authentication tracking is also relaxed in the current demo version, so user-based fields may not be filled.

---

# 4. Java Built-in Sort Can Be Faster

In some benchmark results, Java Built-in Sort may run faster than AAQ.

This is expected because Java Built-in Sort is highly optimized inside the JVM.

AAQ is not designed to beat Java Built-in Sort on every dataset.

AAQ is mainly designed for difficult dataset patterns such as:

```text
Skewed datasets
Repeated-value datasets
Adversarial datasets
Poor-pivot datasets
Structured real-world datasets
```

---

# 5. AAQ Has Extra Overhead

Adaptive Amplitude QuickSort performs extra operations compared with normal QuickSort.

AAQ includes:

```text
Pivot candidate sampling
Partition imbalance estimation
Amplitude weight updates
Interference-style reinforcement
Neighbor propagation
Quantum metric collection
```

Because of this, AAQ may have overhead on simple or uniform datasets.

This is acceptable because the research focus is on improving pivot behavior on difficult datasets.

---

# 6. No Real Quantum Hardware Used

This project does not run on real quantum hardware.

It is a quantum-inspired classical system.

Quantum-inspired means the system uses classical simulations of ideas such as:

```text
Superposition-inspired candidate space
Amplitude-weighted probability
Constructive reinforcement
Destructive suppression
Entanglement-inspired neighbor correlation
QASM/Qiskit-style circuit explanation
```

The Qiskit and OpenQASM outputs are used for research explanation and visualization.

---

# 7. Python Service Must Be Running

Dataset profiling and quantum simulation depend on the Python FastAPI service.

If Python service is not running, these features will fail:

```text
Analyze Dataset
Run Amplitude
Run Interference
Generate OpenQASM
Generate Qiskit
```

Python service must run on:

```text
http://localhost:8000
```

Run command:

```bash
uvicorn main:app --reload --port 8000
```

---

# 8. Backend Must Be Running

The React frontend depends on the Java Spring Boot backend.

If backend is not running, these pages will not load data:

```text
Dashboard
Datasets
Dataset Details
Benchmarks
Reports
System Status
```

Backend must run on:

```text
http://localhost:8080
```

Run command:

```bash
mvn spring-boot:run
```

---

# 9. Large XLSX Preview Can Be Slow

Large XLSX files can take time to preview because Excel files require workbook parsing.

If preview takes too long, the frontend may show timeout.

This is not an algorithm error.

## Workaround

Use smaller CSV files for quick demo testing, or increase Axios timeout.

---

# 10. Current Demo Dataset Limitation

The current demo mainly uses one large XLSX dataset.

For a stronger research evaluation, future testing should include multiple dataset patterns:

```text
Uniform random
Skewed
Nearly sorted
Reverse sorted
Repeated values
Adversarial
Zipf distribution
```

This will help prove when AAQ performs better and when traditional algorithms are stronger.

---

# 11. Recommendation Engine Limitation

The recommendation engine currently uses available dataset profile, detected pattern, benchmark result, and AAQ metrics.

In the current demo, it may recommend AAQ for validation even if Java Built-in Sort is faster.

This is acceptable because AAQ is the proposed research algorithm and baselines are used for validation.

## Future Improvement

Improve recommendation rules using:

```text
Historical benchmark records
Dataset size thresholds
Memory usage
CPU usage
Partition imbalance
Warm amplitude profiles
```

---

# 12. Report File Storage Limitation

Generated reports are stored locally in:

```text
reports/generated/
```

This folder is ignored by Git to avoid pushing generated runtime files.

In a production system, reports should be stored in:

```text
Cloud storage
Database file table
Document storage service
Secure download endpoint
```

---

# 13. Deployment Limitation

This version is built for local development and demo.

It does not include:

```text
Docker
Kubernetes
AWS
Azure
CI/CD
Redis
Kafka
RabbitMQ
Prometheus
Grafana
ELK
```

These are intentionally excluded from the first version to keep the focus on AAQ, dataset profiling, benchmarking, and reports.

---

# 14. Security Limitation

The backend has authentication structure, but strict security enforcement is relaxed for demo testing.

This makes it easier to test all APIs and frontend screens quickly.

For production use, the system should enforce:

```text
JWT validation
Protected backend APIs
Role-based access
Frontend protected routes
Refresh token handling
Secure password policy
```

---

# 15. Local File Storage Limitation

Uploaded datasets are stored in local folders.

This is suitable for demo and development.

For production, dataset files should be moved to:

```text
Cloud object storage
Secure file server
Database-backed file metadata system
```

---

# 16. Benchmark Environment Limitation

Benchmark results depend on local machine performance.

Execution time can change based on:

```text
CPU speed
RAM availability
Background applications
JVM warm-up
Dataset size
File format
Python service load
```

Therefore, benchmark values should be treated as experimental results for the current environment, not universal fixed values.

---

# 17. Final Note

These limitations do not reduce the value of the project.

The main goal of the project is successfully achieved:

```text
Design and implement a Quantum-Inspired Adaptive Sorting Optimization Platform built around the proposed Adaptive Amplitude QuickSort algorithm.
```

The current version demonstrates:

```text
Dataset upload
Dataset analysis
Python profiling
Quantum-inspired simulation
AAQ benchmarking
Baseline comparison
Recommendation
Report generation
Dashboard visualization
System status monitoring
```

Future work can extend this into a fully secured, deployable, multi-user enterprise system.