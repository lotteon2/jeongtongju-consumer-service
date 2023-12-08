package com.jeontongju.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsumerInfoForCreateBySnsRequestDto {

  private Long consumerId;
  private String email;
  private String profileImageUrl;
}
