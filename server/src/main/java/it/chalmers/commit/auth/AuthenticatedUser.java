package it.chalmers.commit.auth;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * The caller of a request, as identified by Gamma. Deliberately lightweight: it is built from the
 * login and held in the session, with no database behind it.
 *
 * <p>It implements {@link OAuth2User} so Spring Security can use it directly as the principal of an
 * OAuth login, which lets controllers receive it through {@link CurrentUser}.
 *
 * @param gammaId Gamma's stable id for the user
 * @param name the user's display name
 */
public record AuthenticatedUser(String gammaId, String name) implements OAuth2User {

  @Override
  public Map<String, Object> getAttributes() {
    return Map.of("sub", gammaId, "name", name);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  /** Spring Security's principal name; the stable Gamma id, not the display name. */
  @Override
  public String getName() {
    return gammaId;
  }
}
