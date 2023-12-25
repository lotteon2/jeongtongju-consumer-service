package com.jeontongju.consumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProfileImageUrlForModifyRequestDto {

  @NotNull(message = "프로필 이미지 정보 없음")
  private String profileImageUrl;
}
