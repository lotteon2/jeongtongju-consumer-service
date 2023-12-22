package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.dto.response.SubscriptionPaymentsInfoForInquiryResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

  public List<SubscriptionPaymentsInfoForInquiryResponseDto> toSubscriptionHistoryInquiryDto(
      Page<Subscription> subscriptionHistoriesPaged) {

    List<SubscriptionPaymentsInfoForInquiryResponseDto> subscriptionHistoryInquiryDto =
        new ArrayList<>();

    for (Subscription subscription : subscriptionHistoriesPaged) {
      SubscriptionPaymentsInfoForInquiryResponseDto build =
          SubscriptionPaymentsInfoForInquiryResponseDto.builder()
              .subscriptionId(subscription.getSubscriptionId())
              .startDate(subscription.getStartDate())
              .endDate(subscription.getEndDate())
              .subscriptionType(subscription.getSubscriptionType())
              .paymentMethod(subscription.getPaymentMethod())
              .paymentAmount(subscription.getPaymentAmount())
              .build();
      subscriptionHistoryInquiryDto.add(build);
    }
    return subscriptionHistoryInquiryDto;
  }
}
