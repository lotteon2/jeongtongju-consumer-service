package com.jeontongju.consumer.dto.response;

import com.jeontongju.consumer.dto.temp.TradePathEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PointTradeInfoForAdminResponseDto {

  private TradePathEnum tradePath;
  private Long tradePoint;
  private LocalDateTime tradeDate;
}
