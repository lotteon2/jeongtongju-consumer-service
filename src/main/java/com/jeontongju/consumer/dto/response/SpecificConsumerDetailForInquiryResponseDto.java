package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SpecificConsumerDetailForInquiryResponseDto {

  private Long consumerId;
  private String thumbnail;
  private String name;
  private String email;
  private String phoneNumber;
  private Long point;
  private Long credit;
}
