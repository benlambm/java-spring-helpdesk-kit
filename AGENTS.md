# AGENTS.md

## Warning: Student Repo

The student must steer this repo. You assist; they decide. Do not write the
TODO method bodies for them. Explain the next failing test and suggest a small
experiment instead of handing over completed implementations.

## What this is

Coding Homework 5, Part B: the Coding Homework 3 help desk rebuilt on Spring
Boot 4.1.1 with `JdbcClient`. Java 21, Maven (wrapper included), H2 for tests
and local runs, PostgreSQL only for the optional deployment.

```text
src/main/java        application code      src/test/java   JUnit 5 tests (the specification)
src/main/resources   schema.sql, application.properties
data/                sample CSV
```

## Don't touch

`pom.xml`, `schema.sql`, `application.properties`, the tests,
`HelpDeskApplication`, `HelpDeskRunner`, `TicketController`, and the sample
data. They define the assignment contract. Never edit a test merely to make it
pass.

## Code style

Java 21, four spaces, package `edu.vwcc.itp220mod4`. Every SQL value goes in as
a `?` parameter through `JdbcClient`, never by string concatenation. Reuse
`TicketRepository.mapRow` for rows that become `Ticket` objects.

## Before you claim it works, run it

```text
./mvnw test
```

Never commit, push, or create branches unless asked.
