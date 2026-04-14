package com.example.bootexceptionhandler.domain.exception;

public class AmountLimitExceededException extends DomainException implements BusinessRuleViolation {
  public AmountLimitExceededException(String message) {
    super(message);
  }
}
