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
public class AddressInfoForModifyRequestDto {

  @NotNull(message = "필수 주소지 정보 미입력")
  private String basicAddress;

  private String addressDetail;

  @NotNull(message = "필수 주소지 정보 미입력")
  private String zonecode;

  @NotNull(message = "필수 주소지 정보 미입력")
  private String recipientName;

  @NotNull(message = "필수 주소지 정보 미입력")
  private String recipientPhoneNumber;

  @NotNull(message = "필수 주소지 정보 미입력")
  private Boolean isDefault;
}
