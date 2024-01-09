package com.jeontongju.consumer.feign;

import io.github.bitbox.bitbox.dto.SubscriptionCouponBenefitForInquiryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponClientService {

  private final CouponServiceClient couponServiceClient;

  @Transactional
  public SubscriptionCouponBenefitForInquiryResponseDto getSubscriptionBenefit(Long consumerId) {

    return couponServiceClient.getSubscriptionBenefit(consumerId).getData();
  }
}
