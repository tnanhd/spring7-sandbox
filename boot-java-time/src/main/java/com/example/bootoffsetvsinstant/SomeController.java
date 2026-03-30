package com.example.bootoffsetvsinstant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/something")
@RequiredArgsConstructor
public class SomeController {

  private final SomeRepository someRepository;

  @PostMapping
  SomeEntity save() {
    final var someEntity =
        SomeEntity.builder()
            .createdAtInstant(Instant.now()) // UTC time always
            .createdAtInstantTz(Instant.now()) // UTC time always
            .createdAtOffsetDateTime(OffsetDateTime.now(ZoneId.of("America/New_York"))) // UTC time always, regardless specified zone id and system's timezone
            .createdAtOffsetDateTimeTz(OffsetDateTime.now(ZoneId.of("America/New_York"))) // UTC time always, regardless specified zone id and system's timezone
            .createdAtLocalDateTime(LocalDateTime.now()) // Must specify UTC zone to store as UTC
            .createdAtLocalDateTimeTz(LocalDateTime.now(ZoneId.of("UTC"))) // Auto convert to UTC time using system's timezone. Not recommended to use this type to store UTC time since weird conversion may happen if system's timezone is not UTC
            .build();
    return someRepository.save(someEntity);
  }
}
