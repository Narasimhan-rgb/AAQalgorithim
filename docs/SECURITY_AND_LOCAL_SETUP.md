# Security and Local Setup

## Important security note

Runtime credentials must not be committed to the repository. The backend reads sensitive values from environment variables.

At minimum set:

```text
AAQ_DB_PASSWORD
AAQ_JWT_SECRET
```

Use a unique PostgreSQL password and a long random JWT secret. If a credential was previously committed to Git history, changing the current file is **not** sufficient: rotate the credential because old commits can still contain it.

## Windows PowerShell example

```powershell
$env:AAQ_DB_PASSWORD = "replace-with-your-local-db-password"
$env:AAQ_JWT_SECRET = "replace-with-a-long-random-secret"
$env:AAQ_DB_URL = "jdbc:postgresql://localhost:5432/quantum"
$env:AAQ_DB_USERNAME = "quantum_admin"
$env:AAQ_DB_SCHEMA = "quantum_schema"
$env:AAQ_PYTHON_SERVICE_URL = "http://localhost:8000"
```

Then start the backend:

```powershell
.\mvnw.cmd spring-boot:run
```

## Frontend

```bash
cd frontend
npm install
npm run dev
```

Create a local frontend `.env` only when required. It is ignored by Git.

## Python service

```bash
cd python-service
py -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
python -m uvicorn main:app --reload --host 127.0.0.1 --port 8000
```

Local database files, generated reports, and datasets are ignored and should not be committed.
