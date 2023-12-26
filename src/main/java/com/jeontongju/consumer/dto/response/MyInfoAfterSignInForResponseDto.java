package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MyInfoAfterSignInForResponseDto {

  private Long memberId;
  private String email;
  private String profileImageUrl;
  private String name;
  private Boolean isAdult;
  private Boolean isRegularPayment;
  private Long point;
  private Long credit;
  private String phoneNumber;
  private Boolean isAddressDefault;
}
