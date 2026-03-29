package com.example.bootoauth2clientlogin.feature.user;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

  // To determine if a user was logged in, return basic sub
  @GetMapping("/me")
  public ResponseEntity<UserInfo> getCurrentUser(@Nullable Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    final var claims =
        Optional.of(authentication)
            .map(OAuth2AuthenticationToken.class::cast)
            .map(OAuth2AuthenticationToken::getPrincipal)
            .map(DefaultOidcUser.class::cast)
            .map(DefaultOidcUser::getClaims);

    final var userInfo =
        claims
            .map(
                c ->
                    UserInfo.of(
                        c.get("sub").toString(),
                        c.get("name").toString(),
                        c.get("email").toString()))
            .orElse(null);

    return ResponseEntity.ok(userInfo);
  }
}
