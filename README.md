# API Test Framework

A **Cucumber BDD** test framework for the
[JSONPlaceholder API](https://jsonplaceholder.org/), built with Java 11,
REST-assured, and JUnit Platform.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [Setup](#setup)
4. [Running the Tests](#running-the-tests)
5. [Test Scenarios](#test-scenarios)
6. [Reports](#reports)
7. [Configuration](#configuration)

---

## Prerequisites

| Tool        | Minimum version |
|-------------|-----------------|
| Java (JDK)  | 11              |
| Maven       | 3.8             |
| Internet    | Required (live API calls to `jsonplaceholder.org`) |

---

## Project Structure

```
api-test-framework/
├── pom.xml                                         # Maven build & dependency config
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── com/api/framework/
        │       ├── context/
        │       │   └── ScenarioContext.java         # Shared state per scenario (PicoContainer DI)
        │       ├── runner/
        │       │   └── TestRunner.java              # JUnit Platform Suite entry point
        │       ├── stepdefinitions/
        │       │   ├── Hooks.java                   # @Before / @After lifecycle hooks
        │       │   └── PostsStepDefinitions.java    # Given / When / Then implementations
        │       └── utils/
        │           ├── ApiClient.java               # REST-assured request spec factory
        │           └── ConfigManager.java           # Config loader (config.properties + system props)
        └── resources/
            ├── config.properties                    # Base URL, timeouts
            └── features/
                └── posts.feature                   # Gherkin feature file
```

---

## Setup

```bash
# 1. Clone / open the project in your IDE (IntelliJ IDEA recommended)
# 2. Install dependencies
mvn clean install -DskipTests
```

---

## Running the Tests

### Run all scenarios

```bash
mvn test
```

### Run a specific tag

```bash
# Smoke suite
mvn test -Dcucumber.filter.tags="@smoke"

# Only the GET scenarios
mvn test -Dcucumber.filter.tags="@get-all or @get-single"

# Single scenario
mvn test -Dcucumber.filter.tags="@create"
```

### Override the base URL (e.g. staging environment)

```bash
mvn test -Dbase.url=https://staging.example.com
```

---

## Test Scenarios

All scenarios target the `/posts` endpoint.

> **Note**: `jsonplaceholder.org` is a read-only fake API. Write operations
> (`POST`, `PUT`, `DELETE`) always return HTTP 200; the response reflects
> pre-seeded fixture data rather than the submitted payload. Tests are
> designed to verify the documented contract of this specific API.

| Tag          | HTTP Method | Endpoint    | What is validated                                                  |
|--------------|-------------|-------------|--------------------------------------------------------------------|
| `@get-all`   | `GET`       | `/posts`    | 200, JSON content-type, non-empty array, required fields present   |
| `@get-single`| `GET`       | `/posts/1`  | 200, `id=1`, non-empty `title`/`content`, positive `userId`        |
| `@create`    | `POST`      | `/posts`    | 200, JSON content-type, response is non-empty list with all fields |
| `@update`    | `PUT`       | `/posts/1`  | 200, JSON content-type, `id=1`, non-empty `title`/`content`        |
| `@delete`    | `DELETE`    | `/posts/1`  | 200, JSON content-type, `id=1`, non-empty `title` in body          |

---

## Reports

After `mvn test`, HTML and JSON reports are written to:

```
target/cucumber-reports/
├── cucumber.html   ← human-readable report (open in browser)
├── cucumber.json   ← machine-readable (CI integration)
└── cucumber.xml    ← JUnit XML (compatible with Jenkins, GitHub Actions, etc.)
```

---

## Configuration

Edit `src/test/resources/config.properties` to change defaults:

```properties
base.url=https://jsonplaceholder.org
connect.timeout=10000
read.timeout=10000
```

Any property can be overridden at runtime with a `-D` Maven system property.
