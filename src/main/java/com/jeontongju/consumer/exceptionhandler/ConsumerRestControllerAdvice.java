package com.jeontongju.consumer.exceptionhandler;

import com.jeontongju.consumer.dto.ErrorFormat;
import com.jeontongju.consumer.exception.DuplicateEmailException;
import com.jeontongju.consumer.utils.CustomErrMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ErrorFormat> handleDuplicateKey() {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorFormat body = ErrorFormat.builder()
        .code(status.value())
        .message(status.name())
        .detail(CustomErrMessage.EMAIL_ALREADY_IN_USE)
        .failure("DUPLICATED_EMAIL")
        .build();
    return ResponseEntity.status(status.value()).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(
                e.getBindingResult().getFieldError() == null
                    ? e.getMessage()
                    : e.getBindingResult().getFieldError().getDefaultMessage())
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }
}
