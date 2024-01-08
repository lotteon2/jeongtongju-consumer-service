package com.jeontongju.consumer.exception;

public class NotSellerAccessDeniedException extends RuntimeException {

  public NotSellerAccessDeniedException(String msg) {
    super(msg);
  }
}
