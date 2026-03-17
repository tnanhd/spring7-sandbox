package com.example.bootformlogin.feature.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @GetMapping("/login-sucess")
  public String loginSuccessPage() {
    return "login-success";
  }
}
