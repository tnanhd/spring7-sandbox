package com.example.bootauth0;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @GetMapping
  public String getMessage() {
    return "Hello from the secured API!";
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_read:messages', 'SCOPE_write:messages')")
  @GetMapping("/scoped")
  public String getScopedMessage() {
    return "Hello from the secured API! This is a scoped message.";
  }
}
