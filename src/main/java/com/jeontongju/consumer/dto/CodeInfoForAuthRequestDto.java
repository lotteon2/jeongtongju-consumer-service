package com.jeontongju.consumer.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CodeInfoForAuthRequestDto {
  @NotNull
  @Length(min = 8, max = 8, message = "유효코드 8자리를 입력해주세요.")
  private String validCode;
}
