package com.jeontongju.consumer.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class CreateConsumerRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
        message = "영문자, 숫자, 특수 문자 모두 포함하여 작성해 주세요."
    )
    @Size(min = 8, max = 16, message = "최소 8자 최대 16자 이하로 작성해 주세요.")
    private String password;

    @NotNull
    @Size(max = 10)
    private String name;
}
