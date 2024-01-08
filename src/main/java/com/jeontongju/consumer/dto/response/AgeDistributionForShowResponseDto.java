package com.jeontongju.consumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AgeDistributionForShowResponseDto {

  private Long teenage;
  private Long twenty;
  private Long thirty;
  private Long fortyOver;

  public void assignTeenage(Long total) {
    this.teenage = total;
  }

  public void assignTwenty(Long total) {
    this.twenty = total;
  }

  public void assignThirty(Long total) {
    this.thirty = total;
  }

  public void assignFortyOver(Long total) {
    this.fortyOver = total;
  }
}
