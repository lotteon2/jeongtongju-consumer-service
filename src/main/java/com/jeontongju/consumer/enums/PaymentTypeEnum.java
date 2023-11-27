package com.jeontongju.consumer.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
    KAKAO("카카오페이"),
    NAVER("네이버페이");

    private final String value;

    PaymentTypeEnum(String value) {
        this.value = value;
    }
}
