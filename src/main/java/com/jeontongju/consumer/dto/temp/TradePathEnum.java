package com.jeontongju.consumer.dto.temp;

import lombok.Getter;

@Getter
public enum TradePathEnum {
  TEXT_REVIEW("리뷰"),
  PURCHASE_USE("구매사용"),
  ORDER_CONFIRMED("주문확정");

  private final String value;

  TradePathEnum(String value) {
    this.value = value;
  }
}
