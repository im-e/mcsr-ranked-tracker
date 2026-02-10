# MCSR Ranked Tracker

Full-stack app that proxies the MCSR Ranked API and presents player stats in a React UI.

## Stack
- Backend: Spring Boot (Web + WebFlux), Java 17
- Frontend: React (CRA), gh-pages for GitHub Pages deploys
- Hosting: Render (backend), GitHub Pages (frontend)

## Project layout
- `src/main/java` - Spring Boot backend
- `src/main/resources/application.properties` - backend config
- `frontend/` - React app
- `render.yaml` - Render service configuration

## API
Base path: `/api/mcsr`
- `GET /api/mcsr/users/{identifier}`
- `GET /api/mcsr/users/{identifier}/stats`
- `GET /api/mcsr/users/{identifier}/stats/by-type`
- `GET /api/mcsr/users/sync/{identifier}/matches`

## Local development
Backend:
```bash
./mvnw spring-boot:run
```

Frontend:
```bash
cd frontend
npm install
npm start
```

By default, the frontend uses CRA `proxy` to reach `http://localhost:8080` in dev.

## Frontend configuration
Set the backend URL at build time (used for GitHub Pages builds):

`frontend/.env.production`
```
REACT_APP_API_BASE=https://mcsr-ranked-tracker.onrender.com
```

## Deployment
Frontend (GitHub Pages):
```bash
cd frontend
npm run build
npm run deploy
```

Backend (Render):
Render uses `render.yaml`. Set `CORS_ALLOWED_ORIGINS` to include the GitHub Pages URL.

## Environment variables
Backend:
- `PORT` (default `8080`)
- `CORS_ALLOWED_ORIGINS` (comma-separated list)

