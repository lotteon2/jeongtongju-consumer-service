package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
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
}
