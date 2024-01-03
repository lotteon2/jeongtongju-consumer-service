package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.dto.response.MyInfoAfterSignInForResponseDto;
import com.jeontongju.consumer.dto.response.PointCreditForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.NameImageForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.TradePathEnum;
import org.springframework.stereotype.Component;

@Component
public class ConsumerMapper {

  public Consumer toEntity(ConsumerInfoForCreateRequestDto createRequestDto) {

    return Consumer.builder()
        .consumerId(createRequestDto.getConsumerId())
        .email(createRequestDto.getEmail())
        .name(createRequestDto.getName())
        .phoneNumber(createRequestDto.getPhoneNumber())
        .build();
  }

  public Consumer toEntity(ConsumerInfoForCreateBySnsRequestDto createByKakaoRequestDto) {

    return Consumer.builder()
        .consumerId(createByKakaoRequestDto.getConsumerId())
        .email(createByKakaoRequestDto.getEmail())
        .profileImageUrl(createByKakaoRequestDto.getProfileImageUrl())
        .build();
  }

  public ConsumerInfoForInquiryResponseDto toInquiryDto(Consumer consumer) {

    return ConsumerInfoForInquiryResponseDto.builder()
        .email(consumer.getEmail())
        .name(consumer.getName())
        .phoneNumber(consumer.getPhoneNumber())
        .profileImageUrl(consumer.getProfileImageUrl())
        .point(consumer.getPoint())
        .credit(consumer.getAuctionCredit())
        .isRegularPayments(consumer.getIsRegularPayment())
        .build();
  }

  public PointCreditForInquiryResponseDto toPointCreditInquiryDto(Consumer consumer) {

    return PointCreditForInquiryResponseDto.builder()
        .point(consumer.getPoint())
        .credit(consumer.getAuctionCredit())
        .build();
  }

  public MyInfoAfterSignInForResponseDto toMyInfoDto(Consumer consumer) {

    return MyInfoAfterSignInForResponseDto.builder()
        .memberId(consumer.getConsumerId())
        .email(consumer.getEmail())
        .profileImageUrl(consumer.getProfileImageUrl())
        .name(consumer.getName())
        .isAdult(consumer.getIsAdult())
        .isRegularPayment(consumer.getIsRegularPayment())
        .isPaymentReservation(consumer.getIsPaymentReservation())
        .point(consumer.getPoint())
        .credit(consumer.getAuctionCredit())
        .phoneNumber(consumer.getPhoneNumber())
        .isAddressDefault(!consumer.getAddressList().isEmpty())
        .build();
  }

  public NameImageForInquiryResponseDto toNameImageDto(Consumer consumer) {

    return NameImageForInquiryResponseDto.builder()
        .name(consumer.getName())
        .imageUrl(consumer.getProfileImageUrl())
        .build();
  }

  public PointHistory toPointHistoryEntity(
      Long tradePoint, TradePathEnum tradePath, Consumer consumer) {

    return PointHistory.builder()
        .tradePoint(tradePoint)
        .tradePathEnum(tradePath)
        .consumer(consumer)
        .build();
  }
}
