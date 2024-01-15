package com.jeontongju.consumer.feign;

import com.jeontongju.consumer.dto.response.CurCouponStatusForReceiveResponseDto;
import com.jeontongju.consumer.dto.temp.FeignFormat;
import io.github.bitbox.bitbox.dto.SubscriptionCouponBenefitForInquiryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "coupon-service")
public interface CouponServiceClient {

  @GetMapping("/consumers/{consumerId}/coupons/benefit")
  FeignFormat<SubscriptionCouponBenefitForInquiryResponseDto> getSubscriptionBenefit(
      @PathVariable Long consumerId);

  @GetMapping("/consumers/{consumerId}/promotion-coupon/prev-check")
  FeignFormat<CurCouponStatusForReceiveResponseDto> prevCheck(@PathVariable Long consumerId);
}
