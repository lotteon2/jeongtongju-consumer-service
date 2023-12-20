package com.jeontongju.consumer.exception;

public class AddressNotFoundException extends RuntimeException {

  public AddressNotFoundException(String msg) {
    super(msg);
  }
}
