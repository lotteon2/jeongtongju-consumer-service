package com.jeontongju.consumer.dto.temp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsumerInfoForAccountConsolidationDto {

  private Long consumerId;
  private String name;
  private String phoneNumber;
}
