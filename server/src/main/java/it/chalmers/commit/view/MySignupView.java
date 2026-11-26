package it.chalmers.commit.view;

import it.chalmers.commit.model.Event;
import it.chalmers.commit.model.User;
import java.util.List;
import java.util.Optional;

/**
 * One of the current user's sign-ups. The status is {@code ATTENDING} or {@code QUEUED}, and the
 * queue position (starting at 1) is only set while queued.
 */
public record MySignupView(EventView event, String status, Integer queuePosition) {

  /** The user's sign-up for the event, or empty if they have none. */
  public static Optional<MySignupView> of(Event event, User user) {
    if (event.getAttendees().stream().anyMatch(s -> s.getUser().getId().equals(user.getId()))) {
      return Optional.of(new MySignupView(EventView.of(event), "ATTENDING", null));
    }
    List<String> queued = event.getQueue().stream().map(s -> s.getUser().getId()).toList();
    int index = queued.indexOf(user.getId());
    if (index < 0) {
      return Optional.empty();
    }
    return Optional.of(new MySignupView(EventView.of(event), "QUEUED", index + 1));
  }
}
