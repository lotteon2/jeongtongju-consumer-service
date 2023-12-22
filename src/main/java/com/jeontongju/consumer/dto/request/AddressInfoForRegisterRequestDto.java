package com.jeontongju.consumer.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressInfoForRegisterRequestDto {

  @NotNull private String addressType;

  @NotNull private String basicAddress;

  private String addressDetail;

  @NotNull private String zonecode;

  @NotNull private String recipientName;

  @NotNull private String recipientPhoneNumber;

  @NotNull private Boolean isDefault;
}
