package com.jeontongju.consumer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SuccessFormat {

  private final Integer code;
  private final String message;
  private final String detail;

  @JsonInclude(Include.NON_NULL)
  private final Object data;
}
