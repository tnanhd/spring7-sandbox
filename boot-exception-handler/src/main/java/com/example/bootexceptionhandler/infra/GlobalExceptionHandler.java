package com.example.bootexceptionhandler.infra;

import com.example.bootexceptionhandler.domain.exception.BusinessRuleViolation;
import com.example.bootexceptionhandler.domain.exception.DomainException;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected @Nullable ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      @Nullable HttpHeaders headers,
      @Nullable HttpStatusCode status,
      @Nullable WebRequest request) {
    final var body = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    body.setTitle("Missing Request Parameter");
    body.setInstance(createInstanceUri(request));
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  record CustomErrorField(String field, @Nullable Object rejectedValue, String message) {}

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleCustomValidationException(Exception ex, WebRequest request)
      throws Exception {
    final var headers = new HttpHeaders();
    if (ex instanceof ConstraintViolationException subEx) {
      return handleConstraintViolationException(subEx, headers, HttpStatus.BAD_REQUEST, request);
    } else {
      throw ex;
    }
  }

  protected ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    final List<CustomErrorField> errors =
        ex.getConstraintViolations().stream()
            .map(
                violation -> {
                  final var path = violation.getPropertyPath().toString();
                  final var fieldName =
                      path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                  return new CustomErrorField(
                      fieldName, violation.getInvalidValue(), violation.getMessage());
                })
            .toList();
    final var body =
        ProblemDetail.forStatusAndDetail(
            statusCode, "Validation failed for " + errors.size() + " field(s).");
    body.setTitle("Validation Rule Violated");
    body.setInstance(createInstanceUri(request));
    body.setProperty("errors", errors);
    return handleExceptionInternal(ex, body, headers, statusCode, request);
  }

  @ExceptionHandler({DomainException.class})
  public ResponseEntity<Object> handleDomainException(Exception ex, WebRequest request) {
    final var headers = new HttpHeaders();
    String title = "Domain Rule Violation";
    if (ex instanceof BusinessRuleViolation) {
      return handleDomainException(ex, headers, title, HttpStatus.UNPROCESSABLE_CONTENT, request);
    } else {
      return handleDomainException(ex, headers, title, request);
    }
  }

  private @Nullable ResponseEntity<Object> handleDomainException(
      Exception ex, HttpHeaders headers, String title, HttpStatusCode status, WebRequest request) {
    final var body = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
    body.setTitle(title);
    body.setInstance(createInstanceUri(request));
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  private @Nullable ResponseEntity<Object> handleDomainException(
      Exception ex, HttpHeaders headers, String title, WebRequest request) {
    return handleDomainException(ex, headers, title, HttpStatus.BAD_REQUEST, request);
  }

  private URI createInstanceUri(@Nullable WebRequest request) {
    return URI.create(request != null ? request.getDescription(false).replace("uri=", "") : "");
  }
}
