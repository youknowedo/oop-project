package it.chalmers.commit.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Lets the fake login endpoint through without being logged in. Only active under {@code dev}. */
@Configuration
@Profile("dev")
class DevSecurityConfig {

  @Bean
  @Order(0)
  SecurityFilterChain devLoginFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/dev/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .csrf(csrf -> csrf.disable());
    return http.build();
  }
}
