package it.chalmers.commit.view;

import it.chalmers.commit.model.Event;
import java.time.LocalDateTime;

/** The JSON shape of an event. */
public record EventView(
    String id,
    String name,
    String description,
    LocalDateTime date,
    LocalDateTime signupDeadline,
    int capacity,
    int availableSpots,
    String status,
    String organizerId) {

  public static EventView of(Event event) {
    return new EventView(
        event.getId(),
        event.getName(),
        event.getDescription(),
        event.getDate(),
        event.getSignupDeadline(),
        event.getCapacity(),
        event.getAvailableSpots(),
        event.getStatus().name(),
        event.getPoster().getId());
  }
}
