package it.chalmers.commit.model;

import java.util.List;
import java.util.Optional;

public interface AllocationStrategy {

  /** Picks who gets the next spot from the queued registrations, in order of arrival. */
  Optional<Registration> nextToPromote(List<Registration> queue);
}
