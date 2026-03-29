package com.example.bootoauth2clientlogin.feature.message;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @PostMapping
  public MessageResponse sendMessage(@RequestBody MessageCommand command) {
    return new MessageResponse("Message received: " + command.message());
  }
}
