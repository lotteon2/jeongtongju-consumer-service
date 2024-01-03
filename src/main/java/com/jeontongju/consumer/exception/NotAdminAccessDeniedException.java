package com.jeontongju.consumer.exception;

public class NotAdminAccessDeniedException extends RuntimeException {

  public NotAdminAccessDeniedException(String msg) {
    super(msg);
  }
}
