# Tinder-mock (Spring Boot + React)

This repository contains a minimal Tinder-like mock app for development and testing. It is intentionally a local mock and does NOT interact with or automate the real Tinder service.

Structure
- `/server` - Spring Boot backend (Maven)
- `/client` - React frontend (Vite) (skeleton)
- `smoke-test.js` - simple Node.js smoke test script (assumes server running at http://localhost:8080)

Quick start (Windows PowerShell)

# Server
cd tinder-springboot/server
mvn -q spring-boot:run

# open client (see client README for dev server)
# or run docker-compose up to build the image

Smoke test (after server is up)
# from repo root
node smoke-test.js

API
- POST /api/auth/register
- POST /api/auth/login
- GET /api/profiles
- GET /api/profiles/{id}
- POST /api/profiles/{id}/swipe
- GET /api/matches
- GET/POST /api/messages/{matchId}
- POST /api/admin/seed (development seed)

Ethics & security
This project is a local mock for development and testing only. Do NOT use it to scrape, automate, or interact with the real Tinder platform or any third-party service. Respect platform ToS and user privacy.

Next steps (suggested)
- Add full JWT request filter to validate tokens on each request.
- Add input validation and DTOs.
- Swap H2 to PostgreSQL and add Flyway migrations.
- Expand React client into a full SPA (login, swipe deck, matches, messaging).

