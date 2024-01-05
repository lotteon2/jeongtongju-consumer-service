package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SubscriptionBenefitForInquiryResponseDto {

  private String name;
  private Long savingAmount;
  private Long pointAcc;
  private Long couponUse;
  private LocalDateTime nextPaymentReservation;
}
