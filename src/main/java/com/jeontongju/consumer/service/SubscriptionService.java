package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.domain.SubscriptionKakao;
import com.jeontongju.consumer.repository.SubscriptionKakaoRespository;
import com.jeontongju.consumer.repository.SubscriptionRespository;
import io.github.bitbox.bitbox.dto.KakaoSubscription;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import io.github.bitbox.bitbox.enums.PaymentMethodEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {
    private final SubscriptionRespository subscriptionRespository;
    private final SubscriptionKakaoRespository subscriptionKakaoRespository;

    public void createSubscription(SubscriptionDto subscriptionDto){
        Subscription subscription = subscriptionRespository.save(
                Subscription.builder()
                        .consumerId(subscriptionDto.getConsumerId())
                        .subscriptionType(subscriptionDto.getSubscriptionType())
                        .paymentAmount(subscriptionDto.getPaymentAmount())
                        .startDate(subscriptionDto.getStartDate())
                        .endDate(subscriptionDto.getEndDate())
                        .paymentMethod(subscriptionDto.getPaymentMethod())
                        .build()
        );

        if(subscriptionDto.getPaymentMethod() == PaymentMethodEnum.KAKAO){
            KakaoSubscription kakaoSubscription = (KakaoSubscription) subscriptionDto.getSubscripton();
             subscriptionKakaoRespository.save(
                     SubscriptionKakao.builder()
                             .kakaoSid(kakaoSubscription.getSid())
                             .kakaoStoreCode(kakaoSubscription.getCid())
                             .kakaoOrderId(kakaoSubscription.getOrderId())
                             .subscription(subscription)
                     .build()
             );
         }
    }


}
