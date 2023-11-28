package com.jeontongju.consumer.exceptionhandler;

import com.jeontongju.consumer.dto.ErrorFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
public class ConsumerRestControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String METHOD_ARGUMENT_VALID_EXCEPTION_MESSAGE = "VALIDATION 오류";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name() + ": " + METHOD_ARGUMENT_VALID_EXCEPTION_MESSAGE)
            .detail(
                e.getBindingResult().getFieldError() == null
                    ? e.getMessage()
                    : e.getBindingResult().getFieldError().getDefaultMessage())
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }
}
