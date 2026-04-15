package com.example.bootauth0;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PingController {

  @RequestMapping
  public String ping() {
    return "pong";
  }
}
