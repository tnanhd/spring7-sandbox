package com.example.bootexceptionhandler.domain.exception;

public class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }
}
