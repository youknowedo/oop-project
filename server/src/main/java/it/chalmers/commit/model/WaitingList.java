package it.chalmers.commit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WaitingList {

  private final List<User> waiting = new ArrayList<>();
  private final WaitingListStrategy strategy;

  public WaitingList(WaitingListStrategy strategy) {
    this.strategy = strategy;
  }

  /** Adds the user to the waiting list unless they are already on it. */
  public void add(User user) {
    if (!waiting.contains(user)) {
      waiting.add(user);
    }
  }

  /** Removes the user from the waiting list. Returns whether they were on it. */
  public boolean remove(User user) {
    return waiting.remove(user);
  }

  /**
   * The user's 1-based place in the list, or empty if they are not on it. This is the order of
   * arrival, so it only matches the admission order for strategies like FIFO.
   */
  public Optional<Integer> positionOf(User user) {
    int index = waiting.indexOf(user);
    return index < 0 ? Optional.empty() : Optional.of(index + 1);
  }

  /** A read-only view of everyone waiting, in order of arrival. */
  public List<User> getWaiting() {
    return Collections.unmodifiableList(waiting);
  }

  public Optional<User> admitNext() {
    Optional<User> next = strategy.next(waiting);
    next.ifPresent(waiting::remove);
    return next;
  }
}
