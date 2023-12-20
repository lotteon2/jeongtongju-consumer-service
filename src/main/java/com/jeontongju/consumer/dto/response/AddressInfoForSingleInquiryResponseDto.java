package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressInfoForSingleInquiryResponseDto {

  private Long addressId;
  private String basicAddress;
  private String addressDetail;
  private String zonecode;
  private String recipientName;
  private String recipientPhoneNumber;
  private Boolean isDefault;
}
