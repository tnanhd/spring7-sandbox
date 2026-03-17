package com.example.bootformlogin.feature.message;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @PreAuthorize("hasRole('USER')")
  @GetMapping
  public String getMessages() {
    return "Hello, this is a protected message!";
  }
}
