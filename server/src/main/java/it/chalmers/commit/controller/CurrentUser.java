package it.chalmers.commit.controller;

import it.chalmers.commit.controller.http.HttpException;
import it.chalmers.commit.controller.http.Request;
import it.chalmers.commit.model.User;
import it.chalmers.commit.model.Users;

/**
 * Finds out who is making a request.
 *
 * <p>TEMPORARY: login is not built yet, so the caller names themselves with an {@code X-User-Id}
 * header. Replace this with the session from the Gamma login, and keep the rest of the controllers
 * as they are.
 */
public class CurrentUser {

  private final Users users;

  public CurrentUser(Users users) {
    this.users = users;
  }

  /** The user making the request, or answers 401 if there is none. */
  public User of(Request request) {
    String id = request.header("X-User-Id");
    if (id == null || id.isBlank()) {
      throw HttpException.unauthorized("Not logged in");
    }
    return users.find(id).orElseThrow(() -> HttpException.unauthorized("Unknown user"));
  }
}
