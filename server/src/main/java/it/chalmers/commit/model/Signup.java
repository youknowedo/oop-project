package it.chalmers.commit.model;

import java.util.Objects;

/**
 * TEMPORARY minimal signup model connecting a user, an event and signup-specific information.
 *
 * TODO(team): Reconcile this file with the full Signup implementation when it is ready. Preserve
 * the public Signup(User, Event, Info) constructor and its non-null checks, or update Event.signUp()
 * to use the agreed replacement. Event does not currently require any Signup getters.
 */
public final class Signup {
  private final User user;
  private final Event event;
  private final Info info;

  public Signup(User user, Event event, Info info) {
    this.user = Objects.requireNonNull(user, "User is required");
    this.event = Objects.requireNonNull(event, "Event is required");
    this.info = Objects.requireNonNull(info, "Signup info is required");
  }

  public User getUser() {
    return user;
  }

  public Event getEvent() {
    return event;
  }

  public Info getInfo() {
    return info;
  }
}
