package com.jeontongju.consumer.dto.temp;

import lombok.Getter;

@Getter
public enum TradePathEnum {
  TEXT_REVIEW("글리뷰"),
  PHOTO_REVIEW("포토리뷰"),
  PURCHASE_USE("구매사용"),
  PURCHASE_CANCEL("구매취소"),
  GENERAL_CONFIRMED("일반 구매확정"),
  YANGBAN_CONFIRMED("양반 구매확정"),
  CHARGE_CREDIT("크레딧 충전"),
  AUCTION_WON("경매 낙찰");

  private final String value;

  TradePathEnum(String value) {
    this.value = value;
  }
}
