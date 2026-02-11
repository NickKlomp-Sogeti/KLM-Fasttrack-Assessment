# FastTrack Holidays assignment

## Assignment context

The FastTrack team has been requested to build a new application that allows crew to (re)schedule their holidays. 
A challenging task! 
The goal is to build a user-friendly self-service application that takes into account applicable business rules (as defined in the labor agreement) and that makes the best possible (efficient) fit with the flight schedule and other crew's holidays. 

Being an agile team we will iterate towards our goal. 
Consider this assignment a first sprint. 
We have set the (sprint) goal to deliver a first working version of the application that allows to view, add, edit and delete holidays.

More detailed user stories can be found below.  

## About the assignment

We are asking you to build a (greenfield) CRUD application with both a backend and a frontend (an SPA; single page application). 
The backend is to be created in Java using Spring Boot (and we have provided a little bit of boilerplate).
We prefer an Angular frontend application or alternatively a Vue.js or React application. 
The communication between backend and frontend is to be done via a RESTFul API.

Try to time-box your effort to 3 hours. 
We ask you to return the outcome as a .zip file and not a link to Google Drive. 
Your deliverable will be used as input for our technical interview, so it should be a good representation of how you work on a daily basis and therefore the assignment is no more than a framework in which you can display your ability. 
We are interested in argumentation behind the choices and solutions you make. 
This can mean that you will not deliver a completely working application, due to the time constraint.

Enjoy taking the assignment! And let us know if you have questions.

## How to use

The boilerplate can be build and executed using the following Maven commands:

```bash
$ mvn clean install
$ mvn spring-boot:run
```

By default, the application will run on `http://localhost:8080`.

## User stories

The following user stories have been planned for this sprint. 
The assignment is completed if the user stories are implemented. 
At least the user stories 1-3 must be resolved in both backend and frontend (minimal viable product). 
Other user stories (4 & 5) are stretched (bonus) goals (and are not necessarily part of the time-box).

1. As a crew member, I want to view an overview of my scheduled holidays.
2. As a crew member, I want to schedule (create) a valid new holiday.
3. As a crew member, I want to cancel (delete) a scheduled holiday.
4. As a crew member, I want to edit a scheduled holiday.
5. As another crew member, I want all of the above and make sure that my holiday does not overlap the one of other crew members.

The following business rules apply:

  * There should be a gap of at least 3 working days between holidays.
  * A holiday must be planned at least 5 working days before the start date.
  * A holiday must be cancelled at least 5 working days before the start date.
  * Holidays must not overlap (for the sake of this assignment also not between different crew members).

## Technical requirements

* Create a git repository to track your history.
* We love clean code!
* Our definition of done includes documentation and tests.
* When applicable, for this assignment, data can be mocked.
* It is not required to make the UI beautiful (but you can :-))
* Holidays should be stored in, and retrieved from, a database (which can be in memory)

A holiday has the following structure (described using json-schema):

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "http://localhost:8080/holiday.schema.json",
  "title": "Holiday",
  "description": "A (to be) scheduled holiday of an employee (crew)",
  "type": "object",
  "properties": {
    "holidayId": {
      "description": "The unique identifier for a holiday",
      "type": "string",
      "format": "uuid"
    },
    "holidayLabel": {
      "description": "The label describing the holidya",
      "type": "string",
      "examples": [ "Summerholidays" ]
    },
    "employeeId": {
      "description": "The unique identifier for a crew member",
      "type": "string",
      "pattern": "^klm[0-9]{6}$",
      "examples": [ "klm012345" ]
    },
    "startOfHoliday": {
      "description": "The start date and time of the holiday (in UTC)",
      "type": "string",
      "format": "date-time",
      "examples": [ "2022-08-02T08:00:00+00:00" ]
    },
    "endOfHoliday": {
      "description": "The end date and time of the holiday (in UTC)",
      "type": "string",
      "format": "date-time",
      "examples": [ "2022-08-16T08:00:00+00:00" ]
    },
    "status": {
      "description": "The status of the holiday",
      "type": "string",
      "enum": ["DRAFT", "REQUESTED", "SCHEDULED", "ARCHIVED"]
    }  
  },
  "required": [ "holidayId", "holidayLabel", "employeeId", "status" ]
}
```

For crew the following structure (an employee) can be used:

```json
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "$id": "http://localhost:8080/employee.schema.json",
  "title": "Employee",
  "type": "object",
  "properties": {
    "employeeId": {
      "description": "The unique identifier for a crew member (klm012345)",
      "type": "string",
      "pattern": "^klm[0-9]{6}$"
    },
    "name": {
      "description": "The name of the employee",
      "type": "string"
    }
  },
  "required": [ "employeeId", "name" ]
}
```
