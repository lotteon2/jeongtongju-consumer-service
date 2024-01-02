package com.jeontongju.consumer.exception;

public class KafkaDuringOrderException extends RuntimeException {

  public KafkaDuringOrderException(String msg) {
    super(msg);
  }
}