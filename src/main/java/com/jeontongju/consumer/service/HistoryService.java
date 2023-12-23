package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.TradePathEnum;
import com.jeontongju.consumer.mapper.HistoryMapper;
import com.jeontongju.consumer.repository.CreditHistoryRepository;
import com.jeontongju.consumer.repository.PointHistoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistoryService {

  private final CreditHistoryRepository creditHistoryRepository;
  private final PointHistoryRepository pointHistoryRepository;
  private final ConsumerService consumerService;
  private final HistoryMapper historyMapper;

  public Page<PointTradeInfoForSingleInquiryResponseDto> getPointHistoriesPaged(
      Consumer consumer, int page, int size) {

    Pageable pageable = getPageableByCreatedAt(page, size);
    Page<PointHistory> pagedHistories = pointHistoryRepository.findByConsumer(consumer, pageable);

    List<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPagedResponseDto =
        historyMapper.toPointHistoriesPagedResponseDto(pagedHistories);
    return new PageImpl<>(
        pointHistoriesPagedResponseDto,
        getPageableByCreatedAt(page, size),
        getPointHistoriesTotalSize(consumer));
  }

  public Page<PointTradeInfoForSingleInquiryResponseDto> getPointSavingHistoriesPaged(
      Consumer consumer, int page, int size) {

    Pageable pageable = getPageableByCreatedAt(page, size);
    Page<PointHistory> pagedSavingHistories =
        pointHistoryRepository.findByConsumerAndTradePointGreaterThan(consumer, 0L, pageable);

    List<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPagedResponseDto =
        historyMapper.toPointHistoriesPagedResponseDto(pagedSavingHistories);
    return new PageImpl<>(
        pointHistoriesPagedResponseDto,
        getPageableByCreatedAt(page, size),
        getPointHistoriesTotalSize(consumer));
  }

  public Page<PointTradeInfoForSingleInquiryResponseDto> getPointUseHistoriesPaged(
      Consumer consumer, int page, int size) {

    Pageable pageable = getPageableByCreatedAt(page, size);
    Page<PointHistory> pagedSavingHistories =
        pointHistoryRepository.findByConsumerAndTradePointLessThan(consumer, 0L, pageable);

    List<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPagedResponseDto =
        historyMapper.toPointHistoriesPagedResponseDto(pagedSavingHistories);

    return new PageImpl<>(
        pointHistoriesPagedResponseDto,
        getPageableByCreatedAt(page, size),
        getPointHistoriesTotalSize(consumer));
  }

  public Pageable getPageableByCreatedAt(int page, int size) {

    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createdAt"));
    return PageRequest.of(page, size);
  }

  public int getPointHistoriesTotalSize(Consumer consumer) {

    return getAllPointHistories(consumer).size();
  }

  public List<PointHistory> getAllPointHistories(Consumer consumer) {

    return pointHistoryRepository.findByConsumer(consumer);
  }

  @Transactional
  public void updateConsumerCredit(Long consumerId, Long credit) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    foundConsumer.assignAuctionCredit(foundConsumer.getAuctionCredit() + credit);

    creditHistoryRepository.save(
        historyMapper.toCreditHistoryEntityByCharge(foundConsumer, credit));
  }

  public long[] calcTotalPointSummary(List<PointHistory> pointHistories) {

    long totalAcc = 0;
    long totalUse = 0;
    for (PointHistory pointHistory : pointHistories) {
      long tradePoint = pointHistory.getTradePoint();

      if (tradePoint < 0) {
        totalUse = totalUse + Math.abs(tradePoint);
      } else {
        totalAcc = totalAcc + tradePoint;
      }
    }
    return new long[] {totalAcc, totalUse};
  }
}
