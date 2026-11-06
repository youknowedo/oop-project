# Architecture

## Frameworks

- Spring Boot for Java server
- SvelteKit for web

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
