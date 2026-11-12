package it.chalmers.commit.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/** Everything requires login except the health check. Unauthenticated requests get a plain 401. */
@Configuration
class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health", "/actuator/health/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .csrf(Customizer.withDefaults());
    return http.build();
  }
}
