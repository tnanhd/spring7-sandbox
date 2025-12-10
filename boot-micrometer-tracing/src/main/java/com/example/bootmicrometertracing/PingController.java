package com.example.bootmicrometertracing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/ping")
public class PingController {

  @GetMapping
  public String ping() {
    log.info("Received ping request");
    return "pong";
  }
}
