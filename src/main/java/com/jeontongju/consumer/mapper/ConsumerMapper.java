package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.*;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.NameImageForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.TradePathEnum;
import io.github.bitbox.bitbox.dto.ConsumerInfoForCreateRequestDto;
import io.github.bitbox.bitbox.dto.ConsumerNameImageDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsumerMapper {

  public Consumer toEntity(ConsumerInfoForCreateRequestDto createRequestDto) {

    return Consumer.builder()
        .consumerId(createRequestDto.getConsumerId())
        .email(createRequestDto.getEmail())
        .name(createRequestDto.getName())
        .age(createRequestDto.getAge())
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

  public MyInfoAfterSignInForResponseDto toMyInfoDto(Consumer consumer, Boolean existSnsAccount) {

    String profileImageUrl =
        consumer.getProfileImageUrl() == null ? "" : consumer.getProfileImageUrl();
    return MyInfoAfterSignInForResponseDto.builder()
        .memberId(consumer.getConsumerId())
        .email(consumer.getEmail())
        .profileImageUrl(profileImageUrl)
        .name(consumer.getName())
        .isAdult(consumer.getIsAdult())
        .isRegularPayment(consumer.getIsRegularPayment())
        .isPaymentReservation(consumer.getIsPaymentReservation())
        .isSocial(existSnsAccount)
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

  public SpecificConsumerDetailForInquiryResponseDto toSpecificConsumerDetailDto(
      Consumer consumer) {

    return SpecificConsumerDetailForInquiryResponseDto.builder()
        .consumerId(consumer.getConsumerId())
        .thumbnail(consumer.getProfileImageUrl())
        .name(consumer.getName())
        .email(consumer.getEmail())
        .phoneNumber(consumer.getPhoneNumber())
        .point(consumer.getPoint())
        .credit(consumer.getAuctionCredit())
        .build();
  }

  public List<ConsumerDetailForSingleInquiryResponseDto> toAllConsumersDto(
      List<Consumer> consumers) {

    List<ConsumerDetailForSingleInquiryResponseDto> allConsumersDtos = new ArrayList<>();
    for (Consumer consumer : consumers) {
      ConsumerDetailForSingleInquiryResponseDto build =
          ConsumerDetailForSingleInquiryResponseDto.builder()
              .consumerId(consumer.getConsumerId())
              .thumbnail(consumer.getProfileImageUrl())
              .name(consumer.getName())
              .email(consumer.getEmail())
              .phoneNumber(consumer.getPhoneNumber())
              .point(consumer.getPoint())
              .credit(consumer.getAuctionCredit())
              .isYangban(consumer.getIsRegularPayment())
              .createdAt(consumer.getCreatedAt())
              .build();
      allConsumersDtos.add(build);
    }
    return allConsumersDtos;
  }

  public ConsumerNameImageDto toUpdatedNameImageDto(Consumer foundConsumer) {

    return ConsumerNameImageDto.builder()
        .name(foundConsumer.getName())
        .imageUrl(foundConsumer.getProfileImageUrl())
        .build();
  }

  public AvailablePointsAtOrderResponseDto toAvailablePointsDto(long availablePoints) {

    return AvailablePointsAtOrderResponseDto.builder().availablePoints(availablePoints).build();
  }
}
