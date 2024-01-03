package com.jeontongju.consumer.dto.response;

import com.jeontongju.consumer.dto.temp.TradePathEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreditTradeInfoForSingleInquiryResponseDto {

  private Long tradeId;
  private Long tradeCredit;
  private TradePathEnum tradePath;
  private LocalDateTime tradeDate;
}
