package com.example.aoplogging.web.rest;

import com.example.aoplogging.service.DemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/greeting")
@RequiredArgsConstructor
public class DemoController {

  private final DemoService demoService;

  @GetMapping
  public String greeting(DemoDto dto) {
    return demoService.greetTheWorld(dto.id());
  }
}
