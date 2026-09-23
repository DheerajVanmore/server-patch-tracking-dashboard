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
├── frontend/          # React application (Vite)
├── backend/           # Spring Boot application (Maven)
├── database/          # SQL schema and seed data
│   ├── schema.sql
│   └── seed.sql
├── tests/selenium/    # [PLANNED] Selenium UI tests
├── docker/            # [PLANNED] Docker configuration
├── ansible/           # [PLANNED] Ansible provisioning
├── docs/              # Documentation
├── .github/           # Issue templates, PR template
├── Jenkinsfile        # CI pipeline definition
├── .gitignore
└── README.md
```

## Prerequisites

- JDK 21+
- Maven 3.9+
- Node.js 22 LTS
- npm 9+
- MySQL 8.4
- Git

## MySQL Setup

1. Ensure MySQL is running on localhost:3306
2. Run the schema:
   ```bash
   mysql -u root -p < database/schema.sql
   ```
3. Load seed data:
   ```bash
   mysql -u root -p < database/seed.sql
   ```

## Backend Setup

```bash
cd backend

# Set environment variables (or create .env based on .env.example)
# DB_HOST=localhost
# DB_PORT=3306
# DB_NAME=server_patch_dashboard
# DB_USERNAME=root
# DB_PASSWORD=your_password

# Build
mvn clean package

# Run
mvn spring-boot:run
# or: java -jar target/dashboard-0.0.1-SNAPSHOT.jar
```

Backend runs on: http://localhost:8080

## Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

Frontend runs on: http://localhost:5173

## How to Run (Full Stack)

1. Start MySQL
2. Run database scripts (schema.sql, seed.sql)
3. Start backend: `cd backend && mvn spring-boot:run`
4. Start frontend: `cd frontend && npm run dev`
5. Open http://localhost:5173

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
# Backend tests
cd backend
mvn clean test

# Build verification
mvn clean package
```

## Maven Commands

| Command | Purpose |
|---------|---------|
| `mvn clean compile` | Compile the project |
| `mvn clean test` | Run unit tests |
| `mvn clean package` | Build JAR artifact |
| `mvn spring-boot:run` | Run the application |

## Current Status

### ✅ Implemented
- Database schema and seed data
- Spring Boot backend with full REST API
- Server CRUD with search/filter
- Patch CRUD with search/filter
- Patch event workflow with state validation
- Dashboard summary with compliance calculation
- Alert generation from application state
- React frontend with all pages
- Frontend-backend integration
- Backend automated tests
- Maven build pipeline
- Jenkinsfile for CI

### 📋 Planned (Future Phases)
- Selenium automated UI tests
- Docker containerization
- Docker Compose orchestration
- Ansible provisioning
- Linux/Ubuntu deployment

## License

This project is a semester project for educational purposes.
