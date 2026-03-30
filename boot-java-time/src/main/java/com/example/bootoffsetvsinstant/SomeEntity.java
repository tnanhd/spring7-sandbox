package com.example.bootoffsetvsinstant;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "some_entity")
public class SomeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "created_at_instant", columnDefinition = "TIMESTAMP")
  Instant createdAtInstant;

  @Column(name = "created_at_instant_tz", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  Instant createdAtInstantTz;

  @Column(name = "created_at_offset_date_time", columnDefinition = "TIMESTAMP")
  OffsetDateTime createdAtOffsetDateTime;

  @Column(name = "created_at_offset_date_time_tz", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  OffsetDateTime createdAtOffsetDateTimeTz;

  @Column(name = "created_at_local_date_time", columnDefinition = "TIMESTAMP")
  LocalDateTime createdAtLocalDateTime;

  @Column(name = "created_at_local_date_time_tz", columnDefinition = "TIMESTAMP WITH TIME ZONE")
  LocalDateTime createdAtLocalDateTimeTz;
}
