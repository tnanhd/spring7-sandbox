package com.example.bootauth0;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuer;

  @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
  private String audience;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    return http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/public").permitAll().anyRequest().authenticated())
        .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
        .build();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    final NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
    final var issuerValidator = new JwtIssuerValidator(issuer);
    final var audienceValidator = new JwtAudienceValidator(audience);
    final OAuth2TokenValidator<Jwt> defaultWithValidators =
        JwtValidators.createDefaultWithValidators(issuerValidator, audienceValidator);
    jwtDecoder.setJwtValidator(defaultWithValidators);
    return jwtDecoder;
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    final var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("permissions"); // Custom claims to convert

    final var jwtAuthenticationConverter = new JwtAuthenticationConverter();
    // Default prefix is "SCOPE_"
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
