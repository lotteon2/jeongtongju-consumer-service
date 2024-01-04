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

  /**
   * 구독권 생성
   *
   * @param subscriptionDto 구독권 정보
   * @param consumer 구독 결제 완료한 회원 객체
   */
  public void createSubscription(SubscriptionDto subscriptionDto, Consumer consumer) {
    Subscription subscription =
        subscriptionRespository.save(subscriptionMapper.toEntity(subscriptionDto, consumer));

    if (subscriptionDto.getPaymentMethod() == PaymentMethodEnum.KAKAO) {
      KakaoSubscription kakaoSubscription = (KakaoSubscription) subscriptionDto.getSubscripton();
      subscriptionKakaoRespository.save(
          subscriptionMapper.toKakaoEntity(kakaoSubscription, subscription));
    }
  }

  /**
   * 구독 결제 내역 가져오기
   *
   * @param consumer 현재 로그인 한 회원 객체
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {Page<SubscriptionPaymentsInfoForInquiryResponseDto>} 한 페이지에 보여줄 구독 결제 내역 정보
   */
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
