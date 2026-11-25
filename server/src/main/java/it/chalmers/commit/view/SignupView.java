package it.chalmers.commit.view;

import it.chalmers.commit.model.Signup;

/** One person's sign-up as the organizer sees it. */
public record SignupView(String userId, String userName) {

  public static SignupView of(Signup signup) {
    return new SignupView(signup.getUser().getId(), signup.getUser().getName());
  }
}
