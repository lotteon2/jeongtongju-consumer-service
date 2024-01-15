package com.jeontongju.consumer.feign;

import com.jeontongju.consumer.dto.response.CurCouponStatusForReceiveResponseDto;
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

  public CurCouponStatusForReceiveResponseDto prevCheck(Long consumerId) {

    return couponServiceClient.prevCheck(consumerId).getData();
  }
}
