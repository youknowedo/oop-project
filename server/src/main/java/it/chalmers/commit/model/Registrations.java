package it.chalmers.commit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Everyone who has signed up, in order of arrival. Each registration is either queued or attending,
 * so the admitted students and the waiting list are two views of the same list.
 */
public class Registrations {

  private final List<Registration> registrations = new ArrayList<>();
  private final AllocationStrategy strategy;

  public Registrations(AllocationStrategy strategy) {
    this.strategy = strategy;
  }

  /** Queues the user unless they are already registered. */
  public void add(User user) {
    if (find(user).isEmpty()) {
      registrations.add(new Registration(user));
    }
  }

  /** Removes the user's registration, queued or attending. Returns whether they had one. */
  public boolean remove(User user) {
    return registrations.removeIf(r -> r.getUser().equals(user));
  }

  /** The user's 1-based place in the admission order, or empty if they are not queued. */
  public Optional<Integer> positionOf(User user) {
    int index = queue().indexOf(find(user).orElse(null));
    return index < 0 ? Optional.empty() : Optional.of(index + 1);
  }

  /** A read-only view of everyone waiting, in the order they will be admitted. */
  public List<Registration> queue() {
    return Collections.unmodifiableList(strategy.order(withStatus(RegistrationStatus.QUEUED)));
  }

  /** A read-only view of everyone admitted, in order of arrival. */
  public List<Registration> attendees() {
    return Collections.unmodifiableList(withStatus(RegistrationStatus.ATTENDING));
  }

  /** Admits the first user in the queue and returns their registration. */
  public Optional<Registration> admitNext() {
    Optional<Registration> next = queue().stream().findFirst();
    next.ifPresent(Registration::admit);
    return next;
  }

  private Optional<Registration> find(User user) {
    return registrations.stream().filter(r -> r.getUser().equals(user)).findFirst();
  }

  private List<Registration> withStatus(RegistrationStatus status) {
    return registrations.stream().filter(r -> r.getStatus() == status).toList();
  }
}
