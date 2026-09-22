# Repository status — 22 September 2026

## Role

Java 21 / Spring Boot backend for classical adaptive sorting, dataset management, jobs and benchmark records. This is independent of the trie-search project.

## Reproduction

Set DATABASE_PASSWORD and JWT_SECRET in your shell; optionally set DATABASE_URL, DATABASE_USER, FILE_STORAGE and PYTHON_SERVICE_URL. Create the quantum database and quantum_schema schema owned by your database user. Run `mvnw.cmd spring-boot:run` on Windows or `sh mvnw spring-boot:run` on Linux.

## Outstanding work

Java 21, PostgreSQL and the Python profiling service are required. The checked-in Spring Boot snapshot dependency still needs Maven resolution. The API currently permits all requests: keep it on loopback for local research. UI login is not backend authorization. A controlled JMH runner and the claimed full 13,500-row measurement artifact are not present in this checkout.

Build or unit-test success is not evidence of a deployed service or a completed research evaluation. See the pull request for checks executed for this revision.

## Checks executed in this pass

Configuration changes inspected. Full Spring Boot build was not executed: this environment has Java 17, while this project requires Java 21 and PostgreSQL. No runtime completion is claimed.

Previously committed database and JWT values remain in Git history. Rotate any values that were used outside a throwaway local environment; this change removes them from the current configuration only.
