package com.jeontongju.consumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressInfoForModifyRequestDto {

  private String basicAddress;
  private String addressDetail;
  private String zonecode;
  private String recipientName;
  private String recipientPhoneNumber;
  private Boolean isDefault;
}
