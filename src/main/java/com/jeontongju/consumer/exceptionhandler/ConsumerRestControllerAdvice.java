package com.jeontongju.consumer.exceptionhandler;

import com.jeontongju.consumer.dto.ErrorFormat;
import com.jeontongju.consumer.dto.response.CurCouponStatusForReceiveResponseDto;
import com.jeontongju.consumer.exception.*;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.ResponseFormat;
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

  private final ConsumerMapper consumerMapper;

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

  @ExceptionHandler(UnsubscribedConsumerException.class)
  public ResponseEntity<ResponseFormat<Void>> handleAlreadyUnsubscribed() {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status)
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(CustomErrMessage.UNSUBSCRIBED_CONSUMER)
                .build());
  }

  @ExceptionHandler(KafkaDuringOrderException.class)
  public ResponseEntity<ResponseFormat<Void>> handleKafkaException() {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ResponseFormat.<Void>builder()
                .code(status.value())
                .message(status.name())
                .detail(CustomErrMessage.ERROR_KAFKA)
                .build());
  }

  @ExceptionHandler(NotOpenPromotionCouponEventException.class)
  public ResponseEntity<ResponseFormat<CurCouponStatusForReceiveResponseDto>>
      handleNotOpenPromotionCouponEvent() {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<CurCouponStatusForReceiveResponseDto>builder()
                .code(HttpStatus.OK.value())
                .data(consumerMapper.toCurCouponStatusDto(false, false, false))
                .build());
  }

  @ExceptionHandler(CouponExhaustedException.class)
  public ResponseEntity<ResponseFormat<CurCouponStatusForReceiveResponseDto>>
      handleCouponExhausted() {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<CurCouponStatusForReceiveResponseDto>builder()
                .code(HttpStatus.OK.value())
                .data(consumerMapper.toCurCouponStatusDto(true, true, false))
                .build());
  }

  @ExceptionHandler(AlreadyReceivePromotionCouponException.class)
  public ResponseEntity<ResponseFormat<CurCouponStatusForReceiveResponseDto>>
      handleAlreadyReceivePromotionCoupon() {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<CurCouponStatusForReceiveResponseDto>builder()
                .code(HttpStatus.OK.value())
                .data(consumerMapper.toCurCouponStatusDto(false, true, true))
                .build());
  }
}
