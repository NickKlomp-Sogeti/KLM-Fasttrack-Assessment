# FastTrack Holidays — Frontend (Angular)

Single Page Application (SPA) for the FastTrack Holidays assignment: view, create and cancel holidays for crew members.

This frontend talks to a Spring Boot backend via REST.

> Assignment context & business rules (gap between holidays, planning/cancel deadlines, no overlap) are defined in the assignment description. :contentReference[oaicite:0]{index=0}  
> In this sprint the UI focuses on the MVP flows (list/create/cancel). Validation/business rules are primarily enforced by the backend.

---

## Tech stack
- Angular (standalone components)
- RxJS
- Reactive Forms
- HttpClient

---

## Prerequisites
- Node.js + npm (project uses npm; see `package.json` for the packageManager version)
- Backend running locally (default: `http://localhost:8080`)

---

## Running locally

### 1) Install dependencies
```bash
npm install
```

### 2) Start the frontend
```bash
npm start
```
OR
```bash
ng serve
```
This will start the Angular development server and open the app in your default browser at `http://localhost:4200`. 

### 3) Ensure backend is running 
Make sure the Spring Boot backend is running locally (default: `http://localhost:8080`) so the frontend can communicate with it. 
--- 
## Notes 
- The UI focuses on core flows (viewing, creating, canceling holidays). Validation and business rules are primarily enforced by the backend. 
- The app uses Angular's HttpClient to make REST calls to the backend API. 
- Reactive Forms are used for the holiday creation form.

