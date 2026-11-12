package it.chalmers.commit.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fake login for local development, only present under the {@code dev} Spring profile. Logs in as
 * any user you name, producing the same {@link AuthenticatedUser} a real Gamma login would.
 */
@RestController
@Profile("dev")
class DevLoginController {

  private final String webUrl;

  DevLoginController(@Value("${web.base-url}") String webUrl) {
    this.webUrl = webUrl;
  }

  @GetMapping("/dev/login")
  void login(
      @RequestParam(defaultValue = "dev-user") String gammaId,
      @RequestParam(defaultValue = "Dev User") String name,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    AuthenticatedUser user = new AuthenticatedUser(gammaId, name);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new OAuth2AuthenticationToken(user, user.getAuthorities(), "dev"));
    SecurityContextHolder.setContext(context);
    request
        .getSession(true)
        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    response.sendRedirect(webUrl);
  }
}
