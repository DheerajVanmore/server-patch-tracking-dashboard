# Server Patch Tracking Dashboard

A centralized web application for IT/operations teams to track server patching information, monitor compliance, and manage patch events across infrastructure.

> **Note:** This is a **patch tracking and visibility system**. It does not remotely install or execute patches on servers.

## Problem Overview

IT operations teams managing multiple servers need a centralized way to:
- Track which patches have been applied to which servers
- Monitor patch compliance across environments
- Identify failed or pending patches requiring attention
- Maintain visibility into the overall patching posture

## Key Features

- **Server Management** — Register and manage server inventory across Production, Staging, and Development environments
- **Patch Tracking** — Catalog patches with severity levels (LOW, MEDIUM, HIGH, CRITICAL)
- **Patch Event Workflow** — Record patch application events with status tracking (PENDING → IN_PROGRESS → PATCHED/FAILED)
- **Dashboard** — Real-time compliance metrics calculated from actual data
- **Alerts** — Auto-generated alerts for critical pending patches and failures
- **Search & Filter** — Find servers, patches, and events quickly
- **Server Drill-Down** — View complete patch history per server

## Architecture

```
User → React Frontend → REST API (JSON) → Spring Boot Backend → MySQL Database
```

| Layer | Technology |
|-------|-----------|
| Frontend | React 18, Vite, React Router, Axios |
| Backend | Java 21, Spring Boot 3.3, Spring Data JPA, Hibernate |
| Database | MySQL 8.4 LTS |
| Build | Maven 3.9, npm |
| CI | Jenkins (Jenkinsfile ready) |

## Repository Structure

```
server-patch-tracking-dashboard/
├── frontend/              # React application (Vite)
│   └── Dockerfile
├── backend/               # Spring Boot application (Maven)
│   └── Dockerfile
├── database/              # SQL schema and seed data
│   ├── schema.sql
│   └── seed.sql
├── tests/selenium/        # Selenium UI/API tests (Python + pytest)
│   ├── test_dashboard.py
│   └── requirements.txt
├── ansible/               # Ansible deployment playbook
│   ├── playbook.yml
│   └── inventory.ini
├── scripts/               # Automation scripts
│   └── health_check.py    # Health check & recovery
├── docs/                  # Documentation
├── .github/               # Issue templates, PR template
├── docker-compose.yml     # Full-stack Docker orchestration
├── Jenkinsfile            # CI/CD pipeline definition
├── .gitignore
└── README.md
```

## Prerequisites

- JDK 21+
- Maven 3.9+
- Node.js 20+
- npm 9+
- MySQL 8.4
- Docker & Docker Compose
- Python 3.10+ (for Selenium tests)
- Git

## Running Locally (without Docker)

1. Ensure MySQL is running on localhost:3306
2. Run the schema and seed data:
   ```bash
   mysql -u root -p < database/schema.sql
   mysql -u root -p < database/seed.sql
   ```
3. Start backend: `cd backend && mvn spring-boot:run`
4. Start frontend: `cd frontend && npm run dev`
5. Open http://localhost:5173

## Running with Docker Compose

```bash
docker compose up -d --build
```

This starts:
- **MySQL 8.4** on port 3306 (auto-seeds schema + data)
- **Spring Boot backend** on port 8080
- **React frontend** (via nginx) on port 5173

To stop: `docker compose down`

## API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/servers` | List/search/filter servers |
| GET | `/api/servers/{id}` | Server detail with patch history |
| POST | `/api/servers` | Create server |
| PUT | `/api/servers/{id}` | Update server |
| DELETE | `/api/servers/{id}` | Delete server |
| GET | `/api/patches` | List/search/filter patches |
| POST | `/api/patches` | Create patch |
| PUT | `/api/patches/{id}` | Update patch |
| DELETE | `/api/patches/{id}` | Delete patch |
| GET | `/api/patch-events` | List/filter patch events |
| POST | `/api/patch-events` | Create patch event |
| PUT | `/api/patch-events/{id}` | Update patch event |
| GET | `/api/dashboard/summary` | Dashboard metrics |
| GET | `/api/alerts` | Generated alerts |

## Testing

```bash
# Backend unit tests (15 tests)
cd backend && mvn clean test

# Selenium tests (requires backend running on port 8080)
pip install -r tests/selenium/requirements.txt
python -m pytest tests/selenium/test_dashboard.py::TestBackendAPI -v
```

## DevOps Pipeline (CI/CD Workflow)

```
GitHub Push
  → Jenkins CI
     → Checkout
     → Maven Compile
     → Unit Tests (15 tests, JUnit reports)
     → Maven Package (JAR)
     → Deploy JAR locally (port 8080)
     → Selenium Tests (3 API tests via headless Chrome)
     → Docker Build & Deploy (docker compose up)
     → Verify deployment (HTTP 200 health check)
  → Ansible Provisioning (docker compose on target)
  → Health Check & Recovery (scripts/health_check.py)
```

### Jenkins Pipeline Stages (Jenkinsfile)

| Stage | Description |
|-------|-------------|
| Checkout | Clone from SCM |
| Build/Compile | `mvn clean compile` |
| Run Tests | `mvn test` + JUnit report |
| Package | `mvn package -DskipTests` |
| Archive JAR | Archive build artifact |
| Deploy JAR | Kill old process, start JAR, verify HTTP 200 |
| Selenium Tests | Run pytest against backend API, publish results |
| Docker Build & Deploy | `docker compose down` → `docker compose up -d --build` → verify |

### Ansible Deployment

```bash
ansible-playbook -i ansible/inventory.ini ansible/playbook.yml
```

Idempotent: re-running does not change already-correct state.

### Health Check & Recovery

```bash
python scripts/health_check.py
```

Checks `/api/dashboard/summary` → if unhealthy, restarts Docker containers → re-verifies.

## Semester DevOps Milestones

| Week | Milestone | Status |
|------|-----------|--------|
| 1–4 | Problem definition, planning, Git setup | ✅ |
| 5–6 | Core feature development, MVP | ✅ |
| 7 | Jenkins CI installation | ✅ |
| 8 | Pipeline as Code + JAR deployment | ✅ |
| 9 | Selenium test design/execution | ✅ |
| 10 | Continuous testing in Jenkins | ✅ |
| 11 | Docker image/container lifecycle | ✅ |
| 12 | Jenkins-Docker CD | ✅ |
| 13 | Ansible configuration management | ✅ |
| 14 | Idempotency, health check, recovery | ✅ |
| 15 | End-to-end release/documentation | ✅ |

## License

This project is a semester project for educational purposes only.
