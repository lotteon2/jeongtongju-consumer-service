package com.jeontongju.consumer.dto.response;

import io.github.bitbox.bitbox.enums.PaymentMethodEnum;
import io.github.bitbox.bitbox.enums.SubscriptionTypeEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SubscriptionPaymentsInfoForInquiryResponseDto {

  private Long subscriptionId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private SubscriptionTypeEnum subscriptionType;
  private PaymentMethodEnum paymentMethod;
  private Long paymentAmount;
}
