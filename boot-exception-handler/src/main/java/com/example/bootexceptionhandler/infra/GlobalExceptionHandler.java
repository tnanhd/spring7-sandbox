package com.example.bootexceptionhandler.infra;

import com.example.bootexceptionhandler.domain.exception.BusinessRuleViolation;
import com.example.bootexceptionhandler.domain.exception.DomainException;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  record CustomErrorField(String field, @Nullable Object rejectedValue, String message) {}

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleExtraValidationException(Exception ex, WebRequest request)
      throws Exception {
    final var headers = new HttpHeaders();
    if (ex instanceof ConstraintViolationException subEx) {
      return handleConstraintViolationException(subEx, headers, HttpStatus.BAD_REQUEST, request);
    } else {
      throw ex;
    }
  }

  // @Validated class and @RequestParam or @PathVariable that has @Min, @Max, etc.
  protected ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex,
      HttpHeaders headers,
      HttpStatusCode status,
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
    return handleErrorListExceptionInternal(ex, headers, status, request, errors);
  }

  // @Valid @RequestBody on method argument and @NotNull, @NotBlank, etc. on dto's field
  @Override
  protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    final List<CustomErrorField> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    new CustomErrorField(
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()))
            .toList();
    return handleErrorListExceptionInternal(ex, headers, status, request, errors);
  }

  // Helper method to handle validation in a list
  private @Nullable ResponseEntity<Object> handleErrorListExceptionInternal(
      Exception ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request,
      List<CustomErrorField> errors) {
    final var body =
        ProblemDetail.forStatusAndDetail(
            status, "Validation failed for " + errors.size() + " field(s).");
    body.setInstance(createInstanceUri(request));
    body.setProperty("errors", errors);
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  // Required request body is missing
  @Override
  protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    final var body =
        ProblemDetail.forStatusAndDetail(
            status,
            Arrays.stream(ex.getMessage().split(":"))
                .findFirst()
                .orElse("Required request body is missing"));
    body.setInstance(createInstanceUri(request));
    return handleExceptionInternal(ex, body, headers, status, request);
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
