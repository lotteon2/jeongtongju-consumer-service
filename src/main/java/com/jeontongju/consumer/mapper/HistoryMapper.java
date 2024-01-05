package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.*;

import java.util.ArrayList;
import java.util.List;

import com.jeontongju.consumer.dto.temp.TradePathEnum;
import com.jeontongju.consumer.utils.PaginationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryMapper {

  private final PaginationManager paginationManager;

  public PointHistory toPointHistoryEntity(
      Consumer consumer, Long accPointByMonth, Long point, TradePathEnum tradePath) {

    return PointHistory.builder()
        .tradePoint(point)
        .tradePathEnum(tradePath)
        .pointAccBySubscription(accPointByMonth)
        .consumer(consumer)
        .build();
  }

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

  public CreditHistory toCreditHistoryEntity(Consumer consumer, Long credit, TradePathEnum tradePathEnum) {

    return CreditHistory.builder()
        .tradeCredit(credit)
        .tradePath(tradePathEnum)
        .consumer(consumer)
        .build();
  }

  public List<CreditTradeInfoForSingleInquiryResponseDto> toCreditHistoriesPagedResponseDto(
      Page<CreditHistory> creditHistoriesPaged) {

    List<CreditTradeInfoForSingleInquiryResponseDto> creditHistoriesPagedList = new ArrayList<>();

    for (CreditHistory creditHistory : creditHistoriesPaged) {

      CreditTradeInfoForSingleInquiryResponseDto build =
          CreditTradeInfoForSingleInquiryResponseDto.builder()
              .tradeId(creditHistory.getTradeId())
              .tradeCredit(creditHistory.getTradeCredit())
              .tradePath(creditHistory.getTradePath())
              .tradeDate(creditHistory.getCreatedAt())
              .build();
      creditHistoriesPagedList.add(build);
    }
    return creditHistoriesPagedList;
  }

  public CreditTradeInfoForSummaryNDetailsResponseDto toCreditSummaryNDetailsResponseDto(
      Long curCredit,
      Long totalAcc,
      Long totalUse,
      Page<CreditTradeInfoForSingleInquiryResponseDto> histories) {

    return CreditTradeInfoForSummaryNDetailsResponseDto.builder()
        .credit(curCredit)
        .totalAcc(totalAcc)
        .totalUse(totalUse)
        .histories(histories)
        .build();
  }

  public Page<PointTradeInfoForAdminResponseDto> toPointHistoriesPagedForAdminResponseDto(
      Page<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPaged,
      int page,
      int size,
      int totalSize) {

    List<PointTradeInfoForAdminResponseDto> pointTradeResponseDtos = new ArrayList<>();
    for (PointTradeInfoForSingleInquiryResponseDto pointHistory : pointHistoriesPaged) {

      PointTradeInfoForAdminResponseDto build =
          PointTradeInfoForAdminResponseDto.builder()
              .tradePath(pointHistory.getTradePath())
              .tradePoint(pointHistory.getTradePoint())
              .tradeDate(pointHistory.getTradeDate())
              .build();
      pointTradeResponseDtos.add(build);
    }

    return paginationManager.wrapByPage(
        pointTradeResponseDtos, paginationManager.getPageableByCreatedAt(page, size), totalSize);
  }

  public Page<CreditTradeInfoForAdminResponseDto> toCreditHistoriesPagedForAdminResponseDto(
      Page<CreditTradeInfoForSingleInquiryResponseDto> creditHistoriesPaged,
      int page,
      int size,
      int totalSize) {

    List<CreditTradeInfoForAdminResponseDto> creditTradeResponseDtos = new ArrayList<>();
    for (CreditTradeInfoForSingleInquiryResponseDto creditHistory : creditHistoriesPaged) {

      CreditTradeInfoForAdminResponseDto build =
          CreditTradeInfoForAdminResponseDto.builder()
              .tradeCredit(creditHistory.getTradeCredit())
              .tradeDate(creditHistory.getTradeDate())
              .build();
      creditTradeResponseDtos.add(build);
    }

    return paginationManager.wrapByPage(
        creditTradeResponseDtos, paginationManager.getPageableByCreatedAt(page, size), totalSize);
  }
}
