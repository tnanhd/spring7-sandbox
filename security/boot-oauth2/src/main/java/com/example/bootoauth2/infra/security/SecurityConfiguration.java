package com.example.bootoauth2.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private static final String[] STATIC_RESOURCES = {
    "/",
    "/index.html",
    "/static/**",
    "/assets/**",
    "/*.json",
    "/*.css",
    "/*.js",
    "/*.png",
    "/*.jpg",
    "/*.ico",
    "/*.svg"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    http.authorizeHttpRequests(
            auth -> auth.requestMatchers(STATIC_RESOURCES).permitAll().anyRequest().permitAll())
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
