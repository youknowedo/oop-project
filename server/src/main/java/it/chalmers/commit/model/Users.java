package it.chalmers.commit.model;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All users, kept in memory for now.
 *
 * <p>TODO(team): Back this with the database once users are persisted and login creates them.
 */
public class Users {

  private final Map<String, User> users = new ConcurrentHashMap<>();

  public void add(User user) {
    users.put(user.getId(), user);
  }

  public Optional<User> find(String id) {
    return Optional.ofNullable(users.get(id));
  }
}
