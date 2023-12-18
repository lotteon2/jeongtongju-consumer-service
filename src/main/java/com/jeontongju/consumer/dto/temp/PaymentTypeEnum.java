package com.jeontongju.consumer.dto.temp;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
  KAKAO("카카오페이");

  private final String value;

  PaymentTypeEnum(String value) {
    this.value = value;
  }
}
