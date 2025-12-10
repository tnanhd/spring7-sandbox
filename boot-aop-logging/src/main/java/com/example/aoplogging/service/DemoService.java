package com.example.aoplogging.service;

import com.example.aoplogging.repository.DemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {

  private final DemoRepository demoRepository;

  public String greetTheWorld(Long id) {
    return demoRepository.getGreeting(id);
  }
}
