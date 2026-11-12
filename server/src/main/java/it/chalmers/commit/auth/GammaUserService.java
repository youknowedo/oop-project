package it.chalmers.commit.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/** Fetches the user's info from Gamma and turns it into an {@link AuthenticatedUser}. */
@Component
class GammaUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
      new DefaultOAuth2UserService();

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    return toAuthenticatedUser(delegate.loadUser(request));
  }

  static AuthenticatedUser toAuthenticatedUser(OAuth2User gammaUser) {
    String id = gammaUser.getName();
    String name = firstPresent(gammaUser, "nickname", "name", "sub");
    return new AuthenticatedUser(id, name);
  }

  private static String firstPresent(OAuth2User user, String... attributes) {
    for (String attribute : attributes) {
      Object value = user.getAttribute(attribute);
      if (value instanceof String s && !s.isBlank()) {
        return s;
      }
    }
    return user.getName();
  }
}
