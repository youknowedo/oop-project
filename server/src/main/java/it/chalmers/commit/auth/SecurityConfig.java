package it.chalmers.commit.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Everything requires login except the health check. Unauthenticated requests get a plain 401; the
 * web app sends the user to {@code /oauth2/authorization/gamma} to start the Gamma login, and is
 * sent back to {@code web.base-url} afterwards.
 */
@Configuration
class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, GammaUserService gammaUserService, @Value("${web.base-url}") String webUrl)
      throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health", "/actuator/health/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .oauth2Login(
            oauth ->
                oauth
                    .userInfoEndpoint(u -> u.userService(gammaUserService))
                    .defaultSuccessUrl(webUrl, true))
        .csrf(Customizer.withDefaults());
    return http.build();
  }
}
