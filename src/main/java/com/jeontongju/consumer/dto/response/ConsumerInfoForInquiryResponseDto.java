package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsumerInfoForInquiryResponseDto {

  private String email;
  private String name;
  private String phoneNumber;
  private String profileImageUrl;
  private Long point;
  private Long credit;
  private Boolean isRegularPayments;
}
