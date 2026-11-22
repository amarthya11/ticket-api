\# Ticket API



!\[CI](https://github.com/amarthya11/ticket-api/actions/workflows/ci.yml/badge.svg)



A REST API for managing support tickets, with priority-based SLA deadlines, status updates, escalation and SLA-breach tracking. Built with Java, Spring Boot and MySQL, and containerised with Docker.



I built this project to practise the kind of workflow I handled in technical support, ticket triage, escalation and SLA tracking, from the engineering side.



\## Tech stack

\- Java 21, Spring Boot, Spring Data JPA, Bean Validation

\- MySQL 8.4

\- Docker and Docker Compose

\- JUnit 5 and Mockito for unit tests

\- GitHub Actions for CI



\## Run it

```

docker compose up -d --build

```

The API is then available at http://localhost:8080.



To run the tests: `mvnw.cmd test` (Windows) or `./mvnw test` (Mac/Linux).



\## Endpoints

| Method | Path | Description |

|---|---|---|

| POST | /tickets | Create a ticket (title, description, priority) |

| GET | /tickets | List all tickets |

| GET | /tickets/{id} | Get one ticket |

| PATCH | /tickets/{id}/status | Update the status |

| POST | /tickets/{id}/escalate | Escalate an open ticket |

| GET | /tickets/breached | List tickets past their SLA deadline |



\## Example

Create a ticket:

```

POST /tickets

{"title": "Router not connecting", "description": "No signal since morning", "priority": "HIGH"}

```

Response (201 Created):

```

{

&#x20; "id": 1,

&#x20; "title": "Router not connecting",

&#x20; "description": "No signal since morning",

&#x20; "priority": "HIGH",

&#x20; "status": "OPEN",

&#x20; "createdAt": "2026-11-21T13:39:05",

&#x20; "slaDueAt": "2026-11-21T21:39:05"

}

```



\## SLA targets

| Priority | Resolve within |

|---|---|

| Critical | 4 hours |

| High | 8 hours |

| Medium | 24 hours |

| Low | 72 hours |



\## Design decisions

\- \*\*Service layer:\*\* The controller only handles HTTP, and the service holds the business rules (SLA calculation and escalation rules). That keeps the rules in one place and lets me unit test them with a mocked repository, without a database.

\- \*\*409 Conflict on escalation:\*\* If a ticket is already resolved or closed, the request is valid and the ticket exists, but its current state doesn't allow the action. 404 would mean the ticket is missing and 400 would mean malformed input, so 409 describes it best.

\- \*\*Global exception handler:\*\* One `@RestControllerAdvice` class turns exceptions into consistent JSON errors (404, 409 and 400 with field messages), so the controllers and service don't need repeated try/catch blocks.

\- \*\*Derived query for breaches:\*\* The repository method `findBySlaDueAtBeforeAndStatusNotIn` is turned into a query by Spring Data JPA from its name, meaning the deadline is earlier than now and the status isn't resolved or closed. That avoids hand-written SQL for a simple condition.

\- \*\*Request objects:\*\* Create and status-update requests use their own record types, so the API doesn't accept fields like `id` or `status` on creation, and validation rules sit on the input.



\## Testing

Unit tests cover the service layer (defaults, SLA deadlines, not-found handling, status updates, escalation rules and the breach query call) and the SLA policy. They run on every push through GitHub Actions.



\## Possible improvements

\- Integration tests with Testcontainers

\- Authentication with Spring Security and JWT

\- Pagination and filtering

\- Moving database credentials into environment variables or secrets

