package com.example.bootformlogin.feature.message;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  @GetMapping
  public String getMessages() {
    return "Hello, this is a protected GET message!";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public String createMessage() {
    return "Hello, this is a protected POST message!";
  }
}
