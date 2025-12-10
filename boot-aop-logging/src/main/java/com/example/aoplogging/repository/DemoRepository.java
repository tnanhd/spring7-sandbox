package com.example.aoplogging.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DemoRepository {
  private static final Map<Long, String> GREETINGS = Map.of(
      1L, "Hello World",
      2L, "Hola Mundo",
      3L, "Bonjour le monde"
  );

  public String getGreeting(Long id) {
    return GREETINGS.getOrDefault(id, "Hello World");
  }
}
