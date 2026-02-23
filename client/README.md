# Tinder Mock Client (React)

This is a minimal React (Vite) client. It expects the backend to be reachable at `/api` (proxy in dev or served from same origin).

Install and run (PowerShell):

```powershell
cd tinder-springboot/client
npm install
npm run dev
```

Note: For development, you can run the Spring Boot server and the Vite dev server concurrently. Configure a proxy in `vite.config.js` to forward `/api` to `http://localhost:8080` if needed.
