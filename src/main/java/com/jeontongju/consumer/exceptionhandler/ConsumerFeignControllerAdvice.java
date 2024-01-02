package com.jeontongju.consumer.exceptionhandler;

import com.jeontongju.consumer.exception.PointInsufficientException;
import com.jeontongju.consumer.exception.PointUsageOver10PercetageException;
import io.github.bitbox.bitbox.dto.FeignFormat;
import io.github.bitbox.bitbox.enums.FailureTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ConsumerFeignControllerAdvice {

  @ExceptionHandler(PointInsufficientException.class)
  public FeignFormat<Void> handleInsufficientPoint() {

    return FeignFormat.<Void>builder()
        .code(HttpStatus.OK.value())
        .failure(FailureTypeEnum.INSUFFICIENT_POINT)
        .build();
  }

  @ExceptionHandler(PointUsageOver10PercetageException.class)
  public FeignFormat<Void> handlePointUsageOver10Percentage() {

    return FeignFormat.<Void>builder()
        .code(HttpStatus.OK.value())
        .failure(FailureTypeEnum.POINT_USAGE_OVER_10_PERCENTAGE)
        .build();
  }
}
