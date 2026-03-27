package com.example.bootoauth2.feature.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @GetMapping
  public String getMessage() {
    return "Hello, this is a protected message!";
  }
}
