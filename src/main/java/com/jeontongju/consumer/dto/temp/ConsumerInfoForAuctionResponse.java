package com.jeontongju.consumer.dto.temp;

import com.jeontongju.consumer.domain.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ConsumerInfoForAuctionResponse {

  private String name;
  private String profileImage;
  private Long credit;

  public static ConsumerInfoForAuctionResponse toDto(Consumer consumer) {
    return ConsumerInfoForAuctionResponse.builder()
        .name(consumer.getName())
        .profileImage(consumer.getProfileImageUrl())
        .credit(consumer.getAuctionCredit())
        .build();
  }
}
