package it.chalmers.commit.view;

import it.chalmers.commit.model.Event;
import java.util.List;

/** Everyone signed up for an event: the attendees and the queue in the order of admission. */
public record SignupsView(List<SignupView> attendees, List<SignupView> queue) {

  public static SignupsView of(Event event) {
    return new SignupsView(
        event.getAttendees().stream().map(SignupView::of).toList(),
        event.getQueue().stream().map(SignupView::of).toList());
  }
}
