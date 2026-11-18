package it.chalmers.commit.model;

import java.util.List;
import java.util.Optional;

public interface WaitingListStrategy {

  Optional<User> next(List<User> waiting);
}
