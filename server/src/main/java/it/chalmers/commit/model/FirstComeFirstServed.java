package it.chalmers.commit.model;

import java.util.List;

/** Admits people in the order they signed up. */
public class FirstComeFirstServed implements AllocationStrategy {

  @Override
  public List<Registration> order(List<Registration> queue) {
    return List.copyOf(queue);
  }
}
