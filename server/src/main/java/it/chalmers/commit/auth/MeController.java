package it.chalmers.commit.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Tells the web app who is logged in. */
@RestController
class MeController {

  /** What the API exposes of the principal; kept apart from the Spring Security view of it. */
  record MeResponse(String gammaId, String name) {}

  @GetMapping("/me")
  MeResponse me(@CurrentUser AuthenticatedUser user) {
    return new MeResponse(user.gammaId(), user.name());
  }
}
