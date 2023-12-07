package com.jeontongju.consumer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SuccessFeignFormat<T> {

  private final Integer code;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final T data;
}
