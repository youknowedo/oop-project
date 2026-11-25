package it.chalmers.commit.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All events, kept in memory for now.
 *
 * <p>TODO(team): Back this with the database once events are persisted.
 */
public class Events {

  private final Map<String, Event> events = new ConcurrentHashMap<>();

  public void add(Event event) {
    events.put(event.getId(), event);
  }

  public Optional<Event> find(String id) {
    return Optional.ofNullable(events.get(id));
  }

  public void remove(Event event) {
    events.remove(event.getId());
  }

  /** Every event, soonest first. */
  public List<Event> all() {
    return events.values().stream().sorted(java.util.Comparator.comparing(Event::getDate)).toList();
  }
}
