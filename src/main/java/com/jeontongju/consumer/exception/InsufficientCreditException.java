package com.jeontongju.consumer.exception;

public class InsufficientCreditException extends RuntimeException {

  public InsufficientCreditException(String msg) {
    super(msg);
  }
}
