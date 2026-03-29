package com.example.bootoauth2clientlogin.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] STATIC_RESOURCES = {
    "/", "/index.html", "/assets/**", "/*.svg", "/api/users/me"
  };

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) {
    http.csrf(CsrfConfigurer::spa);

    http.authorizeHttpRequests(
        requests -> {
          requests.requestMatchers(STATIC_RESOURCES).permitAll();
          requests.anyRequest().authenticated();
        });

    http.oauth2Login(login -> login.defaultSuccessUrl("/", true));
    http.logout(
        logout -> {
          var logoutSuccessHandler =
              new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
          logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
          logout.logoutSuccessHandler(logoutSuccessHandler);
        });

    return http.build();
  }

  interface AuthoritiesConverter
      extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {}

  @Bean
  AuthoritiesConverter authoritiesConverter() {
    return claims -> {
      var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
      var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
      return roles
          .map(List::stream)
          .orElse(Stream.empty())
          .map(SimpleGrantedAuthority::new)
          .map(GrantedAuthority.class::cast)
          .toList();
    };
  }

  @Bean
  GrantedAuthoritiesMapper authenticationConverter(AuthoritiesConverter authoritiesConverter) {
    return authorities ->
        authorities.stream()
            .filter(OidcUserAuthority.class::isInstance)
            .map(OidcUserAuthority.class::cast)
            .map(OidcUserAuthority::getIdToken)
            .map(OidcIdToken::getClaims)
            .map(authoritiesConverter::convert)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
  }
}
