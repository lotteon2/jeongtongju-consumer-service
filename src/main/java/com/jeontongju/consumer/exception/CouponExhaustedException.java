package com.jeontongju.consumer.exception;

public class CouponExhaustedException extends RuntimeException {

  public CouponExhaustedException(String msg) {
    super(msg);
  }
}
