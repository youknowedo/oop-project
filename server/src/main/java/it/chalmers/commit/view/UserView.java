package it.chalmers.commit.view;

import it.chalmers.commit.model.User;
import java.util.List;

/** The JSON shape of a user. */
public record UserView(String id, String name, String role, List<String> allergies) {

  public static UserView of(User user) {
    return new UserView(
        user.getId(), user.getName(), user.getRole().name(), List.copyOf(user.getAllergies()));
  }
}
