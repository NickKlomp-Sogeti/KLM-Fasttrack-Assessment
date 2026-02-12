# FastTrack Holidays – KLM Assessment

## Overview

This project is a Spring Boot backend application developed as part of the KLM FastTrack technical assessment.

The current implementation covers:

### User Story 1
> *As a crew member, I want to view an overview of my scheduled holidays.*

### User Story 2
> *As a crew member, I want to schedule (create) a valid new holiday.*

The application provides REST endpoints to retrieve and create holiday data, including validation of business rules.

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
  Contains business logic and validation rules.

- **Repository**  
  Handles database interaction using Spring Data JPA.

A DTO layer is used to prevent exposing JPA entities directly.

Global exception handling is implemented using `@RestControllerAdvice` to return consistent HTTP error responses.

Seed data is provided via `data.sql` to allow immediate testing.

---

## Implemented Endpoints

### GET `/holidays`

Returns all holidays.

### GET `/holidays/employee/{employeeId}`

Returns holidays for a specific employee.

### POST `/holidays`

Creates a new holiday.

### DELETE `/holidays/{holidayId}` 
Deletes a holiday by ID.

#### Example Request

```json
{
  "holidayLabel": "Summerholidays",
  "employeeId": "klm012345",
  "startOfHoliday": "2099-02-10T08:00:00Z",
  "endOfHoliday": "2099-02-20T08:00:00Z"
}
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

## Future Improvements
- Implement authentication and authorization.
- Soft delete for holidays instead of hard delete.
- Automatic logging of holiday creation and deletion.
- Database migration using Flyway or Liquibase.
- More comprehensive unit and integration tests.