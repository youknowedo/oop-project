package it.chalmers.commit.controller;

import it.chalmers.commit.controller.http.Response;
import it.chalmers.commit.controller.http.Router;
import it.chalmers.commit.model.Event;
import it.chalmers.commit.model.Events;
import it.chalmers.commit.model.User;
import it.chalmers.commit.view.MySignupView;
import it.chalmers.commit.view.UserView;
import java.util.ArrayList;
import java.util.List;

/** The current user's own information and sign-ups. */
public class UserController {

  /** The body of {@code PUT /api/me}. A field that is left out is not changed. */
  record UpdateMe(String name, List<String> allergies) {}

  private final CurrentUser currentUser;
  private final Events events;

  public UserController(CurrentUser currentUser, Events events) {
    this.currentUser = currentUser;
    this.events = events;
  }

  public void register(Router router) {
    router.get("/api/me", request -> Response.ok(UserView.of(currentUser.of(request))));

    router.put(
        "/api/me",
        request -> {
          User user = currentUser.of(request);
          UpdateMe update = request.body(UpdateMe.class);
          synchronized (user) {
            if (update.name() != null) {
              user.updateName(update.name());
            }
            if (update.allergies() != null) {
              for (String allergy : new ArrayList<>(user.getAllergies())) {
                user.removeAllergy(allergy);
              }
              update.allergies().forEach(user::addAllergy);
            }
            return Response.ok(UserView.of(user));
          }
        });

    router.get(
        "/api/me/signups",
        request -> {
          User user = currentUser.of(request);
          List<MySignupView> signups = new ArrayList<>();
          for (Event event : events.all()) {
            synchronized (event) {
              MySignupView.of(event, user).ifPresent(signups::add);
            }
          }
          return Response.ok(signups);
        });
  }
}
