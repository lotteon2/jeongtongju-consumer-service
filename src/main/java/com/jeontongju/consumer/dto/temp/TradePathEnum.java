package com.jeontongju.consumer.dto.temp;

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
