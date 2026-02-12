# FastTrack Holidays – KLM Assessment

## Overview

This project is a Spring Boot backend application developed as part of the KLM FastTrack technical assessment.

The current implementation covers **User Story 1**:

> *As a crew member, I want to view an overview of my scheduled holidays.*

The application provides REST endpoints to retrieve holiday data and supports filtering by employee.

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- H2 (in-memory database)
- Swagger / OpenAPI
- JUnit 5 + Mockito

---

## Architecture

The application follows a layered architecture:

Controller → Service → Repository → Database

Responsibilities are separated as follows:

- **Controller**  
  Handles HTTP requests and responses.

- **Service**  
  Contains application logic and maps entities to DTOs.

- **Repository**  
  Handles database interaction using Spring Data JPA.

To prevent exposing persistence models directly, a DTO layer is used for API responses.

Seed data is provided via `data.sql` to allow immediate testing.

---

## Implemented Endpoints

### GET `/holidays`

Returns all holidays.

### GET `/holidays/employee/{employeeId}`

Returns holidays for a specific employee.

### Example Response

```json
[
  {
    "holidayId": "0f8fad5b-d9cb-469f-a165-70867728950e",
    "holidayLabel": "Summerholidays",
    "employeeId": "klm012345",
    "startOfHoliday": "2022-08-02T08:00:00Z",
    "endOfHoliday": "2022-08-16T08:00:00Z",
    "status": "SCHEDULED"
  }
]
```
---
## Running the Application

### Build the project
```bash ./mvnw clean install ``` 

### Run the application 
```bash ./mvnw spring-boot:run ``` 
The application will start on `http://localhost:8080`.

### Access Swagger UI 
Navigate to `http://localhost:8080/swagger-ui.html` 
to explore the API documentation and test endpoints. 

