package com.jeontongju.consumer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsumerInfoForSignupRequestDto {

  @NotNull
  @Email(message = "사용자 이메일/이름 또는 비밀번호 형식이 잘못되었습니다.")
  private String email;

  @NotNull
  @Size(max = 10, message = "사용자 이메일/이름 또는 비밀번호 형식이 잘못되었습니다.")
  private String name;
}
