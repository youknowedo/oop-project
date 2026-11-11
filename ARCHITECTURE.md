# Architecture

## Overview

Monorepo with two separately built apps:

```
server/   Spring Boot REST backend (Java 17, Maven)
web/      SvelteKit frontend (TypeScript, pnpm)
```

The web app talks to the server over HTTP. The server owns all domain logic and persistence.

## Stack

### Server (`server/`)

- Spring Boot 4.1 (Java 17), package `it.chalmers.commit`
- Spring Web MVC for the HTTP layer, Spring Security for auth, Bean Validation for input checks
- Spring Data JPA with PostgreSQL (H2 for tests)
- Actuator for health/metrics
- Database connection is configured via `DB_URL`, `DB_USER`, `DB_PASSWORD` (defaults to a local `oop` database)

### Web (`web/`)

- SvelteKit 3 with Svelte 5, built with Vite and served by `adapter-node` (`pnpm build`, then `pnpm start`)
- Tailwind CSS v4 for styling
- shadcn-svelte (style `nova`, neutral base, lucide icons) for UI components, primary color `#09CDDA`
- Import aliases: `#lib` -> `src/lib`, components in `#lib/components`, UI primitives in `#lib/components/ui`
- Geist as the font

## Web-server communication

> Proposed, not implemented yet. Items marked TODO(team) need a decision.

- The server exposes a JSON REST API under `/api`. The web app is the only client.
- The web app reaches the server through SvelteKit server code (`+page.server.ts`, `+server.ts`), not directly from the browser. The browser only talks to SvelteKit, so the server needs no CORS setup and its address stays private.
- The server base URL comes from an environment variable on the web side (e.g. `API_URL`, default `http://localhost:8080`).
- TODO(team): confirm this proxy approach versus calling the API directly from the browser.

## Authentication (Gamma)

> Proposed, not implemented yet. Gamma's user fields and group data are still to be verified.

Login uses Gamma (the IT section's account system) over OAuth 2.0 authorization code flow.

1. The user clicks "Log in" on the web app and is redirected to Gamma.
2. Gamma redirects back with an authorization code.
3. The code is exchanged for tokens and the user is identified (stable id, name, and membership info if Gamma provides it).
4. The user is stored or updated on our side and gets a session.
5. Later requests from the web app to the server carry that session.

- TODO(team): decide who owns the OAuth flow. Option A: Spring Security acts as the OAuth client and the web app forwards the session cookie. Option B: SvelteKit handles the flow and the server validates a token. Option A keeps all security logic in one place.
- Roles (e.g. organizer vs. student) are decided on the server, never trusted from the web app.
- Local development needs either a Gamma dev client or a fake login, so the app can run without access to Gamma.

## Local development and deployment

Requirements: Java 17, Node 24 (see `web/.nvmrc`), pnpm, PostgreSQL.

| Part | Command | Default address |
| --- | --- | --- |
| Database | PostgreSQL with a database named `oop` | `localhost:5432` |
| Server | `cd server && ./mvnw spring-boot:run` | `localhost:8080` |
| Web | `cd web && pnpm install && pnpm dev` | `localhost:5173` |

- Server DB settings: `DB_URL`, `DB_USER`, `DB_PASSWORD`. The defaults match a local Postgres with user and password `postgres`.
- Tests use the in-memory H2 database (`./mvnw test`).
- Production web build: `cd web && pnpm build && pnpm start` (runs `node build`).
- Production server build: `cd server && ./mvnw package`, then run the jar with the DB variables set.
- TODO(team): decide where this is hosted and whether to add Docker and CI.

## Requirements

- Event posting R
- Event sign up R
- Event CRUD R
- Queueing system R
- Saved information for end users R
- Login with Gamma R
- Notifications and reminders R

### Nice to have

- Filter and sort events
- Ticket verification N
- Contacting organizer N
- Social credit system N
- Stop abuse (limit sign ups and posting)

## Classes

```java
class Master = {
    db: DbClient;
    events: Events[];

    createEvent();
    changeEvent();
    deleteEvent();

    createRequest();
    ...,
}


class User = {
    id: string;
    name: string;
    role: Role;
    allergies: string[];
};

class Event = {
    id: string;
    poster: User;
    name: string;
    description: string;
    date: DateTime;
    attendees: Request[];
    queue: Request[];
};

class Request = {
    user: User;
    event: Event;
    info: Info;
}

class NotificationSystem = {
    observers: NotificationObserver[];

    subscribe(observer: NotificationObserver);
    update(message: string) {
        for (NotificationObserver observer : observers) {
            observer.update(message);
        }
    }
}

class NotificationObserver = {
    update(message: string);
}

class EmailNotifier extends NotificationObserver = {
    update(message: string);
}


```

## API

> Draft. Paths, methods and rules are a starting point for the team to agree on. All endpoints are under `/api` and require login unless noted.

| Method | Path | Purpose | Who |
| --- | --- | --- | --- |
| GET | `/events` | List events (later: filter and sort) | Any user |
| POST | `/events` | Create an event | Organizer |
| GET | `/events/{id}` | Event details | Any user |
| PUT | `/events/{id}` | Update an event | Event's organizer |
| DELETE | `/events/{id}` | Delete an event | Event's organizer |
| POST | `/events/{id}/signups` | Sign up for an event. Joins the queue if the event is full | Any user |
| DELETE | `/events/{id}/signups/me` | Cancel own sign-up. Next in queue is promoted | Signed-up user |
| GET | `/events/{id}/signups` | See attendees and queue | Event's organizer |
| GET | `/me` | Current user and saved information (e.g. allergies) | Any user |
| PUT | `/me` | Update saved information | Any user |
| GET | `/me/signups` | The user's sign-ups and queue positions | Any user |

Notifications and reminders are sent by the server and have no endpoints for now. TODO(team): add endpoints if users get to choose notification settings.
