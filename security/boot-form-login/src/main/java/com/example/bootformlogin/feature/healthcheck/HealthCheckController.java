package com.example.bootformlogin.feature.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

  @GetMapping("/public/ping")
  public String ping() {
    return "Running";
  }
}
