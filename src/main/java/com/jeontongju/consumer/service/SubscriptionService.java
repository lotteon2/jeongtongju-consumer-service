package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.domain.SubscriptionKakao;
import com.jeontongju.consumer.dto.response.SubscriptionPaymentsInfoForInquiryResponseDto;
import com.jeontongju.consumer.mapper.SubscriptionMapper;
import com.jeontongju.consumer.repository.SubscriptionKakaoRespository;
import com.jeontongju.consumer.repository.SubscriptionRespository;
import io.github.bitbox.bitbox.dto.KakaoSubscription;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import io.github.bitbox.bitbox.enums.PaymentMethodEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

  private final SubscriptionRespository subscriptionRespository;
  private final SubscriptionKakaoRespository subscriptionKakaoRespository;
  private final SubscriptionMapper subscriptionMapper;

  public void createSubscription(SubscriptionDto subscriptionDto, Consumer consumer) {
    Subscription subscription =
        subscriptionRespository.save(
            Subscription.builder()
                .consumer(consumer)
                .subscriptionType(subscriptionDto.getSubscriptionType())
                .paymentAmount(subscriptionDto.getPaymentAmount())
                .startDate(subscriptionDto.getStartDate())
                .endDate(subscriptionDto.getEndDate())
                .paymentMethod(subscriptionDto.getPaymentMethod())
                .build());

    if (subscriptionDto.getPaymentMethod() == PaymentMethodEnum.KAKAO) {
      KakaoSubscription kakaoSubscription = (KakaoSubscription) subscriptionDto.getSubscripton();
      subscriptionKakaoRespository.save(
          SubscriptionKakao.builder()
              .kakaoSid(kakaoSubscription.getSid())
              .kakaoStoreCode(kakaoSubscription.getCid())
              .kakaoOrderId(kakaoSubscription.getOrderId())
              .subscription(subscription)
              .build());
    }
  }

  public Page<SubscriptionPaymentsInfoForInquiryResponseDto> getSubscriptionHistories(
      Consumer consumer, int page, int size) {

    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createdAt"));
    Pageable pageable = PageRequest.of(page, size);

    Page<Subscription> subscriptionHistoriesPaged =
        subscriptionRespository.findByConsumer(consumer, pageable);
    List<SubscriptionPaymentsInfoForInquiryResponseDto> subscriptionHistoriesInquiryDto =
        subscriptionMapper.toSubscriptionHistoryInquiryDto(subscriptionHistoriesPaged);

    return new PageImpl<>(
        subscriptionHistoriesInquiryDto,
        pageable,
        subscriptionRespository.findByConsumer(consumer).size());
  }
}
