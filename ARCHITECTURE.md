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

```
/event/[id]
```
