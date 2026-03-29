package com.example.bootoauth2clientlogin.feature.user;

public record UserInfo(
    String sub,
    String name,
    String email
) {

  public static UserInfo of(String sub, String name, String email) {
    return new UserInfo(sub, name, email);
  }
}
