# FastTrack Holidays – Fullstack CRUD Application

## 📌 Overview

This project represents the first sprint implementation of the **FastTrack Holidays** application.

The goal of the application is to provide crew members with a self-service platform to manage their holidays in a structured and rule-compliant manner.

This implementation delivers a minimal viable product (MVP) supporting:

- Viewing scheduled holidays
- Creating a new holiday
- Deleting a holiday

The following business rules are enforced:

- Holidays must not overlap (including between different crew members)
- A minimum gap of 3 working days between holidays is required
- A holiday must be scheduled at least 5 working days before its start date
- A holiday must be cancelled at least 5 working days before its start date

The focus of this sprint is on clean architecture, maintainability, and correct business rule implementation rather than UI styling.

---

## 🏗️ Architecture

This is a fullstack CRUD application consisting of:

- **Backend**: Java + Spring Boot (REST API)
- **Frontend**: Angular (Single Page Application)
- **Database**: In-memory H2 database

Frontend and backend communicate through RESTful HTTP endpoints using JSON.

```
root
├── backend
└── frontend
```

---

## 🖥 Backend

The backend is built using Spring Boot and follows a layered architecture.

Business logic is separated from controllers and implemented in dedicated service classes.

### Key Characteristics

- RESTful API design
- Layered architecture (Controller → Service → Repository)
- DTO-based request/response handling
- Validation at the API boundary
- Business rule enforcement in the service layer
- JPA for persistence
- In-memory H2 database

The backend runs by default on:

http://localhost:8080

---

## 💻 Frontend

The frontend is implemented as a Single Page Application using Angular.

### Key Characteristics

- Component-based architecture
- Dedicated service layer for API communication
- Reactive forms with validation
- Clear separation between UI logic and data access
- Functional and minimalistic UI

The frontend runs by default on:

http://localhost:4200

---

## ⚙️ How to Run the Application

### 1️⃣ Start the Backend

Navigate to the backend directory:

```bash
cd backend
```

Build and run the Spring Boot application:

```bash
mvn clean install
mvn spring-boot:run
```
### 2️⃣ Start the Frontend
Navigate to the frontend directory:

```bash
cd frontend
```
Install dependencies and start the Angular application:

```bash
npm install
```

Start the development server:

```bash
ng serve
```
---

### API
Base endpoint example:

http://localhost:8080/holidays

Supported operations:

- GET /holidays – Retrieve all holidays
- POST /holidays – Create a new holiday
- DELETE /holidays/{id} – Delete a holiday
- The API follows REST principles and uses JSON for request/response bodies.
