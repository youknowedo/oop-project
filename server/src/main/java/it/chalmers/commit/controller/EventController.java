package it.chalmers.commit.controller;

import it.chalmers.commit.controller.http.HttpException;
import it.chalmers.commit.controller.http.Request;
import it.chalmers.commit.controller.http.Response;
import it.chalmers.commit.controller.http.Router;
import it.chalmers.commit.model.Event;
import it.chalmers.commit.model.EventStatus;
import it.chalmers.commit.model.Events;
import it.chalmers.commit.model.Info;
import it.chalmers.commit.model.User;
import it.chalmers.commit.model.UserRole;
import it.chalmers.commit.view.EventView;
import it.chalmers.commit.view.SignupsView;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The event endpoints, including signing up. The rules (capacity, queue, deadlines) live in {@link
 * Event}, so the handlers only check who is allowed, call the model and answer with a view.
 *
 * <p>An {@link Event} is not safe to use from several threads, and the server handles requests in
 * parallel, so every handler works on an event inside {@code synchronized (event)}.
 */
public class EventController {

  /** The body of {@code POST /api/events} and {@code PUT /api/events/{id}}. */
  record EventBody(
      String name,
      String description,
      LocalDateTime date,
      int capacity,
      LocalDateTime signupDeadline) {}

  private final CurrentUser currentUser;
  private final Events events;

  public EventController(CurrentUser currentUser, Events events) {
    this.currentUser = currentUser;
    this.events = events;
  }

  public void register(Router router) {
    router.get(
        "/api/events",
        request -> {
          currentUser.of(request);
          return Response.ok(events.all().stream().map(this::view).toList());
        });

    router.post(
        "/api/events",
        request -> {
          User user = currentUser.of(request);
          requireOrganizer(user);
          EventBody body = request.body(EventBody.class);
          Event event =
              new Event(
                  UUID.randomUUID().toString(),
                  user,
                  body.name(),
                  body.description(),
                  body.date(),
                  body.capacity(),
                  body.signupDeadline());
          events.add(event);
          return Response.created(view(event));
        });

    router.get(
        "/api/events/{id}",
        request -> {
          currentUser.of(request);
          return Response.ok(view(find(request)));
        });

    router.put(
        "/api/events/{id}",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          EventBody body = request.body(EventBody.class);
          synchronized (event) {
            requireOrganizerOf(event, user);
            event.updateEvent(
                body.name(),
                body.description(),
                body.date(),
                body.capacity(),
                body.signupDeadline());
            return Response.ok(EventView.of(event));
          }
        });

    // Not in the API draft, but without it an event can never leave DRAFT and be signed up for.
    router.post(
        "/api/events/{id}/publish",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          synchronized (event) {
            requireOrganizerOf(event, user);
            event.publishEvent();
            return Response.ok(EventView.of(event));
          }
        });

    router.delete(
        "/api/events/{id}",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          synchronized (event) {
            requireOrganizerOf(event, user);
            if (event.getStatus() == EventStatus.DRAFT) {
              events.remove(event);
              return Response.ok(java.util.Map.of("deleted", true));
            }
            event.cancelEvent();
            return Response.ok(EventView.of(event));
          }
        });

    router.post(
        "/api/events/{id}/signups",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          synchronized (event) {
            // TODO(team): pass the real Info once its fields are agreed.
            event.signUp(user, new Info());
            return Response.created(
                it.chalmers.commit.view.MySignupView.of(event, user).orElseThrow());
          }
        });

    router.delete(
        "/api/events/{id}/signups/me",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          synchronized (event) {
            event.cancelSignup(user);
            return Response.ok(EventView.of(event));
          }
        });

    router.get(
        "/api/events/{id}/signups",
        request -> {
          User user = currentUser.of(request);
          Event event = find(request);
          synchronized (event) {
            requireOrganizerOf(event, user);
            return Response.ok(SignupsView.of(event));
          }
        });
  }

  private EventView view(Event event) {
    synchronized (event) {
      return EventView.of(event);
    }
  }

  private Event find(Request request) {
    return events
        .find(request.pathParam("id"))
        .orElseThrow(() -> HttpException.notFound("Event not found"));
  }

  private static void requireOrganizer(User user) {
    if (user.getRole() != UserRole.ORGANIZER) {
      throw HttpException.forbidden("Only organizers can do this");
    }
  }

  private static void requireOrganizerOf(Event event, User user) {
    requireOrganizer(user);
    if (!event.getPoster().getId().equals(user.getId())) {
      throw HttpException.forbidden("Only the event's organizer can do this");
    }
  }
}
