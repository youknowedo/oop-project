package it.chalmers.commit.model;

import java.util.List;

public interface AllocationStrategy {

  /**
   * The queued registrations in the order they will be admitted. The input is in order of arrival.
   */
  List<Registration> order(List<Registration> queue);
}
