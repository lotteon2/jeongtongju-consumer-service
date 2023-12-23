package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSummaryNDetailsResponseDto;
import java.util.ArrayList;
import java.util.List;

import com.jeontongju.consumer.dto.temp.TradePathEnum;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class HistoryMapper {

  public List<PointTradeInfoForSingleInquiryResponseDto> toPointHistoriesPagedResponseDto(
      Page<PointHistory> histories) {

    List<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPagedResponseDto =
        new ArrayList<>();
    for (PointHistory pointHistory : histories) {
      PointTradeInfoForSingleInquiryResponseDto build =
          PointTradeInfoForSingleInquiryResponseDto.builder()
              .tradeId(pointHistory.getTradeId())
              .tradePoint(pointHistory.getTradePoint())
              .tradePath(pointHistory.getTradePathEnum())
              .tradeDate(pointHistory.getCreatedAt())
              .build();
      pointHistoriesPagedResponseDto.add(build);
    }
    return pointHistoriesPagedResponseDto;
  }

  public PointTradeInfoForSummaryNDetailsResponseDto toPointSummaryNDetailsResponseDto(
      Long curPoint,
      Long totalAcc,
      Long totalUse,
      Page<PointTradeInfoForSingleInquiryResponseDto> histories) {

    return PointTradeInfoForSummaryNDetailsResponseDto.builder()
        .point(curPoint)
        .totalAcc(totalAcc)
        .totalUse(totalUse)
        .histories(histories)
        .build();
  }

  public CreditHistory toCreditHistoryEntityByCharge(Consumer consumer, Long credit) {

    return CreditHistory.builder()
        .tradeCredit(credit)
        .tradePath(TradePathEnum.CHARGE_CREDIT)
        .consumer(consumer)
        .build();
  }
}
