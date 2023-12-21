package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.dto.response.MyInfoAfterSignInForResponseDto;
import com.jeontongju.consumer.dto.response.PointCreditForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
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
        .email(consumer.getEmail())
        .profileImageUrl(consumer.getProfileImageUrl())
        .name(consumer.getName())
        .isAdult(consumer.getIsAdult())
        .isRegularPayment(consumer.getIsRegularPayment())
        .point(consumer.getPoint())
        .credit(consumer.getAuctionCredit())
        .phoneNumber(consumer.getPhoneNumber())
        .isAddressDefault(!consumer.getAddressList().isEmpty())
        .build();
  }
}
