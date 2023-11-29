package com.jeontongju.consumer.enums;

import lombok.Getter;

@Getter
public enum TradePathEnum {
  REVIEW("리뷰"),
  ORDER_CONFIRMED("주문확정");

  private final String value;

  TradePathEnum(String value) {
    this.value = value;
  }
}
