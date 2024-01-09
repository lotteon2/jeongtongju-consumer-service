package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.domain.SubscriptionKakao;
import com.jeontongju.consumer.dto.response.SubscriptionBenefitForInquiryResponseDto;
import com.jeontongju.consumer.dto.response.SubscriptionPaymentsInfoForInquiryResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.github.bitbox.bitbox.dto.KakaoSubscription;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

  public Subscription toEntity(SubscriptionDto subscriptionDto, Consumer consumer) {

    return Subscription.builder()
        .consumer(consumer)
        .subscriptionType(subscriptionDto.getSubscriptionType())
        .paymentAmount(subscriptionDto.getPaymentAmount())
        .startDate(subscriptionDto.getStartDate())
        .endDate(subscriptionDto.getEndDate())
        .paymentMethod(subscriptionDto.getPaymentMethod())
        .build();
  }

  public SubscriptionKakao toKakaoEntity(
      KakaoSubscription kakaoSubscription, Subscription subscription) {

    return SubscriptionKakao.builder()
        .kakaoSid(kakaoSubscription.getSid())
        .kakaoStoreCode(kakaoSubscription.getCid())
        .kakaoOrderId(kakaoSubscription.getOrderId())
        .subscription(subscription)
        .build();
  }

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

  public SubscriptionBenefitForInquiryResponseDto toSubscriptionBenefitDto(
      String name, Long pointAcc, Long couponUse, LocalDateTime nextPaymentReservation) {

    return SubscriptionBenefitForInquiryResponseDto.builder()
        .name(name)
        .savingAmount(pointAcc + couponUse)
        .pointAcc(pointAcc)
        .couponUse(couponUse)
        .nextPaymentReservation(nextPaymentReservation)
        .build();
  }
}
