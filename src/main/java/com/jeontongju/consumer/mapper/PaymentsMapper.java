package com.jeontongju.consumer.mapper;

import io.github.bitbox.bitbox.dto.CreditUpdateDto;
import io.github.bitbox.bitbox.dto.KakaoPayCancelDto;
import org.springframework.stereotype.Component;

@Component
public class PaymentsMapper {

  public KakaoPayCancelDto toKakaoPayCancelDto(CreditUpdateDto creditUpdateDto) {

    return KakaoPayCancelDto.builder()
        .tid(creditUpdateDto.getTid())
        .cancelAmount(creditUpdateDto.getCancelAmount())
        .cancelTaxFreeAmount(creditUpdateDto.getCancelTaxFreeAmount())
        .build();
  }
}
