# Architecture

## Overview

Monorepo with two separately built apps:

```
server/   REST backend (Java 17, Maven)
web/      SvelteKit frontend (TypeScript, pnpm)
```

The web app talks to the server over HTTP. The server owns all domain logic and persistence.

## Stack

### Server (`server/`)

- Java 17, package `it.chalmers.commit`, built with Maven into one runnable jar (`java -jar target/server.jar`)
- No application framework. HTTP is the JDK's own `com.sun.net.httpserver.HttpServer`, with a small `Router` on top
- Gson for JSON
- Hibernate ORM (without Spring) with PostgreSQL, and Flyway for schema migrations in `src/main/resources/db/migration`. Hibernate only validates the schema, it never changes it
- Settings come from environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD` (default to a local `oop` database), `PORT` (default 8080) and `WEB_ORIGIN` (the web app's origin for CORS, default `http://localhost:5173`)
- All routes live under `/api`, and errors are JSON like `{"error": "message"}`

#### Packages

- `model`: the domain objects and all database access. A `Database` class owns the Hibernate `SessionFactory`, and collection-style classes (for example an `Events` class) use it, so nothing outside the model sees a Hibernate session. The model may use any library but must not depend on `view` or `controller`
- `view`: the JSON representations of the model, so model objects are never sent directly
- `controller`: the HTTP handlers. They parse the request, call the model and answer with a view

Dependencies run `controller -> view -> model`. These rules are checked by `ArchitectureTests`.

### Web (`web/`)

- SvelteKit 3 with Svelte 5, built with Vite and served by `adapter-node` (`pnpm build`, then `pnpm start`)
- Tailwind CSS v4 for styling
- shadcn-svelte (style `nova`, neutral base, lucide icons) for UI components, primary color `#09CDDA`
- Import aliases: `#lib` -> `src/lib`, components in `#lib/components`, UI primitives in `#lib/components/ui`
- Geist as the font

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

```
/event/[id]
```
