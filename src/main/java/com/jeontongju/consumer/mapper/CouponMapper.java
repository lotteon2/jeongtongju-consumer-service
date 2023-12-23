package com.jeontongju.consumer.mapper;

import io.github.bitbox.bitbox.dto.ConsumerRegularPaymentsCouponDto;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

  public ConsumerRegularPaymentsCouponDto toConsumerRegularPaymentsCouponDto(
      SubscriptionDto subscriptionDto) {
    return ConsumerRegularPaymentsCouponDto.builder()
        .consumerId(subscriptionDto.getConsumerId())
        .successedAt(subscriptionDto.getStartDate())
        .build();
  }
}
