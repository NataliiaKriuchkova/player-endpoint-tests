# Player API Test Automation

[![CI](https://github.com/NataliiaKriuchkova/player-endpoint-tests/actions/workflows/allure.yml/badge.svg)](https://github.com/NataliiaKriuchkova/player-endpoint-tests/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-blue)](https://nataliiakriuchkova.github.io/player-endpoint-tests/)

Automation framework for testing **Player API**.

API documentation:  
http://3.68.165.45/swagger-ui.html#/player-controller

The project was implemented as part of an **AQA technical assignment**.

---

## Technology Stack

- Java 11
- Maven
- TestNG
- REST Assured
- Jackson
- Allure
- Log4j2
- GitHub Actions
- Checkstyle

---

## Framework Features

The framework includes:

- API client layer
- Service layer
- Request / response models
- Test data generation
- Assertions layer
- Logging
- Configuration management
- Parallel execution
- Allure reporting
- CI integration

---

## Project Structure

```
src/test/java/com/player/
├── assertions/
├── client/
├── config/
├── core/
├── mapper/
├── model/
├── service/
├── tests/
├── testsupport/
└── utils/
```

| Layer | Purpose |
|-------|---------|
| `client` | HTTP calls to API endpoints |
| `service` | Business logic wrapper over client |
| `mapper` | Conversion between models and requests |
| `assertions` | Reusable assertion methods |
| `utils` | Test data generation and providers |
| `core` | Base test infrastructure |

---

## Test Coverage

| Controller | Positive | Negative |
|------------|----------|----------|
| Create Player | + | + |
| Get Player | + | + |
| Get All Players | + | - |
| Update Player | + | + |
| Delete Player | + | + |

Tests cover:

- CRUD operations
- Validation rules (age, password, gender, role)
- Role-based authorization
- API contract verification

---

## Parallel Execution

Tests run in parallel using TestNG:

```xml
parallel="methods"
thread-count="3"
```

Configuration: `src/test/resources/testng.xml`

---

## Test Groups

| Group | Purpose |
|-------|---------|
| `smoke` | Core functionality |
| `negative` | Validation and rejection tests |
| `create` | Player creation tests |
| `bug` | Tests that expose known API defects |

---

## Logging

Logging is implemented with **Log4j2**.

Configuration: `src/test/resources/log4j2.xml`

Logs include request payloads, response bodies, HTTP status codes, and cleanup operations.

---

## Configuration

Framework configuration is externalized to:

```
src/test/resources/config.properties
```

```properties
app.base.url=http://3.68.165.45
app.supervisor.login=supervisor
app.admin.login=admin
```

---

## Allure Report

Live report: https://nataliiakriuchkova.github.io/player-endpoint-tests/

Generate report:

```bash
mvn allure:report
```

Open report locally:

```bash
mvn allure:serve
```

The report includes test results, execution timeline, severity levels, and categorized defects.

---

## How to Run Tests Locally

### Prerequisites

Make sure the following tools are installed:

- Java 11+
- Maven 3.9+
- Git

Verify installation:

```bash
java -version
mvn -v
```

### Clone Repository

```bash
git clone https://github.com/NataliiaKriuchkova/player-endpoint-tests.git
cd player-endpoint-tests
```

### Run Tests

Execute all tests:

```bash
mvn clean test
```

Run tests with full Maven lifecycle (includes validation and reporting):

```bash
mvn clean verify
```

Run a specific test group:

```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=negative
```

### Generate Allure Report

```bash
mvn allure:report
```

### Open Allure Report

```bash
mvn allure:serve
```

---

## CI

Tests run automatically via **GitHub Actions**.

Pipeline steps:

1. Checkout repository
2. Setup Java
3. Run Maven tests
4. Generate Allure report
5. Publish report to GitHub Pages

Workflow: `.github/workflows/allure.yml`

---

## Bugs Found

The test suite discovered two categories of bugs.

---

### Bugs Covered by Autotests

These bugs are visible in Allure under **Product defects**.

---

#### BUG-SC01 - Create response returns null fields

**Severity:** Minor  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Response contains all fields sent in the request.

**Actual:** Response returns `null` for all fields except `id` and `login`:

```json
{
  "id": 674173172,
  "login": "user_123",
  "password": null,
  "screenName": null,
  "gender": null,
  "age": null,
  "role": null
}
```

---

#### BUG-SC06 - Duplicate login is accepted

**Severity:** Critical  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Request with an already existing `login` is rejected with `400`.

**Actual:** Request is accepted with `200`. The existing player data is silently overwritten.

---

#### BUG-SC07 - Duplicate screenName is accepted

**Severity:** Major  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Request with an already existing `screenName` is rejected with `400`.

**Actual:** Request is accepted with `200`. A new player is created with a duplicate `screenName`.

---

#### BUG-SC09 - Age above maximum is accepted

**Severity:** Major  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** `age=60` is rejected with `400` (valid range: 17-59).

**Actual:** Player is created with `age=60`.

---

#### BUG-SC10 - Invalid password is accepted

**Severity:** Major  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Passwords that violate rules are rejected with `400`.

**Actual:** Invalid passwords are accepted and player is created.

Password rules per spec:
- Latin letters and digits only
- Minimum 7 characters
- Maximum 15 characters

---

#### BUG-SC11 - Invalid gender values are accepted

**Severity:** Minor  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Only `male` and `female` are accepted. Any other value is rejected with `400`.

**Actual:** Values such as `unknown`, `Male`, `MALE`, `FEMALE`, `transgender`, `123` are accepted.

---

#### BUG-SC12 - Uppercase role values are accepted

**Severity:** Minor  
**Endpoint:** `GET /player/create/{editor}`

**Expected:** Only `admin` and `user` are valid roles. `ADMIN` and `USER` should be rejected with `400`.

**Actual:** `ADMIN` and `USER` are accepted and players are created with these roles.

---

#### BUG-DEL02 - Deleted player returns 200 instead of 404

**Severity:** Major  
**Endpoint:** `POST /player/get`  
**Scenario:** Retrieving a deleted player

**Expected:** Returns `404 Not Found`.

**Actual:** Returns `200` with an empty response body.

---

### Architecture and Security Bugs

These bugs represent design and security issues that cannot be reliably covered by functional autotests, but pose serious risks in a production environment.

---

#### ARCH-01 - GET method used for resource creation

**Severity:** Major  
**Endpoint:** `GET /player/create/{editor}`

The `GET` method is used to create a new resource. Per HTTP specification, `GET` must be safe (no side effects) and idempotent.

Creating a resource must use `POST`:

```
POST /player
```

**Risks:** Proxy caching may cause duplicate creation. Breaks REST semantics.

---

#### ARCH-02 - Password transmitted as query parameter

**Severity:** Critical  
**Endpoint:** `GET /player/create/{editor}`

**Request:**
```
GET /player/create/supervisor?password=kbnao505&login=...
```

Query parameters are stored in browser history, server access logs, proxy logs, and CDN logs.

**Correct approach:** Password must be sent in the request body over HTTPS:

```json
{
  "login": "...",
  "password": "..."
}
```

---

#### ARCH-03 - Password returned in GET response

**Severity:** Critical  
**Endpoint:** `POST /player/get`

**Response:**
```json
{
  "id": 123,
  "login": "user_abc",
  "password": "kbnao505"
}
```

Passwords must never be returned by an API. Even if hashed, returning the hash enables offline brute-force attacks.

**Correct approach:** Omit the `password` field from all read responses.

---

#### ARCH-04 - Password stored in plaintext

**Severity:** Critical

The password is transmitted and returned as plaintext, which indicates it is stored without hashing.

**Correct approach:** Passwords must be hashed using a strong algorithm such as bcrypt before storage and must never be recoverable.

---

#### ARCH-05 - Authorization via path parameter

**Severity:** Critical  
**Endpoint:** `GET /player/create/{editor}`

The caller identity is passed as a plain path parameter. Any client can impersonate any user by changing the value:

```
/player/create/supervisor
/player/create/admin
```

There is no actual authentication. The server cannot verify that the caller is who they claim to be.

**Correct approach:** Use Authorization header with a token such as JWT or Basic Auth.

---

#### ARCH-06 - HTTP 200 returned instead of 201 on creation

**Severity:** Trivial  
**Endpoint:** `GET /player/create/{editor}`

Per REST conventions, successful resource creation should return `201 Created` with a `Location` header:

```
HTTP/1.1 201 Created
Location: /player/674173172
```

**Actual:** Returns `HTTP 200` with no `Location` header.

---

#### ARCH-07 - Delete does not return 404 for non-existing player

**Severity:** Minor  
**Endpoint:** `DELETE /player/delete/{editor}`

| Case | Expected status |
|------|----------------|
| Delete success | `204 No Content` |
| Player not found | `404 Not Found` |

**Actual:** The endpoint may return `200` even when the player does not exist.

---

#### ARCH-08 - Password is not required on player creation

**Severity:** Critical  
**Endpoint:** `GET /player/create/{editor}`

Per Swagger specification, `password` is the only field marked as `required: false`. All other fields are required. This means a player can be created without a password.

**Correct approach:** Password must be a required field on creation.

---

#### ARCH-09 - Update endpoint allows role change

**Severity:** Critical  
**Endpoint:** `PATCH /player/update/{editor}/{id}`

`PlayerUpdateRequestDto` contains a `role` field. However, the API documentation explicitly states that only the following fields can be updated:

```
age, gender, login, password, screenName
```

`role` is not in the list. If the API accepts role changes through this endpoint, any user could potentially escalate their own privileges.

---

#### ARCH-10 - Get all players endpoint requires no authentication

**Severity:** Major  
**Endpoint:** `GET /player/get/all`

The endpoint is publicly accessible. Any anonymous client can retrieve the full list of players including their `id`, `role`, `screenName`, `age`, and `gender` without any authentication.

---

#### ARCH-11 - Inconsistent response contracts across endpoints

**Severity:** Minor  
**Endpoints:** `GET /player/get/all` vs `POST /player/get`

`GET /player/get/all` returns `PlayerItem` which does not include `login`. `POST /player/get` returns `PlayerGetByPlayerIdResponseDto` which does include `login`. The same resource is represented differently depending on the endpoint, which creates an inconsistent API contract.

---

## Author

Nataliia Kriuchkova