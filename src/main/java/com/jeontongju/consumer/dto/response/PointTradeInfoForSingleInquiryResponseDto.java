package com.jeontongju.consumer.dto.response;

import com.jeontongju.consumer.dto.temp.TradePathEnum;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PointTradeInfoForSingleInquiryResponseDto {

  private Long tradeId;
  private Long tradePoint;
  private TradePathEnum tradePath;
  private Timestamp tradeDate;
}
