package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.response.CreditTradeInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.dto.response.CreditTradeInfoForSummaryNDetailsResponseDto;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSummaryNDetailsResponseDto;
import com.jeontongju.consumer.mapper.HistoryMapper;
import com.jeontongju.consumer.repository.CreditHistoryRepository;
import com.jeontongju.consumer.repository.PointHistoryRepository;
import com.jeontongju.consumer.utils.PaginationManager;
import io.github.bitbox.bitbox.dto.CreditUpdateDto;
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

  private final PaginationManager<PointTradeInfoForSingleInquiryResponseDto> pointPaginationManager;

  private final PaginationManager<CreditTradeInfoForSingleInquiryResponseDto>
      creditPaginationManager;

  /**
   * 포인트 요약 정보 및 내역 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param search 필터링 기준
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {PointTradeInfoForSummaryNDetailsResponseDto} 포인트 내역 요약 및 세부 정보
   */
  public PointTradeInfoForSummaryNDetailsResponseDto getMyPointSummaryNDetails(
      Long consumerId, String search, int page, int size) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    // 포인트 거래내역 가져오기 (한 페이지 만큼)
    Page<PointTradeInfoForSingleInquiryResponseDto> pointHistoriesPaged =
        getPointHistoriesPaged(foundConsumer, search, page, size);

    List<PointHistory> pointHistories = foundConsumer.getPointHistoryList();
    // 포인트 요약 정보 계산하기
    long[] summary = calcPointSummary(pointHistories);

    return historyMapper.toPointSummaryNDetailsResponseDto(
        foundConsumer.getPoint(), summary[0], summary[1], pointHistoriesPaged);
  }

  /**
   * 한 페이지만큼의 포인트 거래 내역 조회 (+필터링)
   *
   * @param consumer 현재 로그인 한 회원 객체
   * @param search 필터링 기준
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {Page<PointTradeInfoForSingleInquiryResponseDto>} 한 페이지에 보여줄 포인트 거래 내역
   */
  public Page<PointTradeInfoForSingleInquiryResponseDto> getPointHistoriesPaged(
      Consumer consumer, String search, int page, int size) {

    Pageable pageable = pointPaginationManager.getPageableByCreatedAt(page, size);

    Page<PointHistory> pagedHistories = null;
    if ("acc".equals(search)) {
      pagedHistories =
          pointHistoryRepository.findByConsumerAndTradePointGreaterThan(consumer, 0L, pageable);
    } else if ("use".equals(search)) {
      pagedHistories =
          pointHistoryRepository.findByConsumerAndTradePointLessThan(consumer, 0L, pageable);
    } else if (search == null) {
      pagedHistories = pointHistoryRepository.findByConsumer(consumer, pageable);
    }

    List<PointTradeInfoForSingleInquiryResponseDto> histories =
        historyMapper.toPointHistoriesPagedResponseDto(pagedHistories);

    int totalSize = pointHistoryRepository.findByConsumer(consumer).size();
    return pointPaginationManager.wrapByPage(histories, pageable, totalSize);
  }

  /**
   * 포인트 요약 정보 계산
   *
   * @param histories 로그인 한 회원의 모든 포인트 거래 내역
   * @return {long[]} 첫번째 인자: 총 적립액, 두번째 인자: 총 사용액
   */
  public long[] calcPointSummary(List<PointHistory> histories) {

    long totalAcc = 0;
    long totalUse = 0;
    for (PointHistory history : histories) {
      long tradeAmount = history.getTradePoint();

      if (tradeAmount < 0) {
        totalUse = totalUse + Math.abs(tradeAmount);
      } else {
        totalAcc = totalAcc + tradeAmount;
      }
    }
    return new long[] {totalAcc, totalUse};
  }

  /**
   * 크레딧 요약 정보 및 내역 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {CreditTradeInfoForSummaryNDetailsResponseDto} 크레딧 내역 요약 및 세부 정보
   */
  public CreditTradeInfoForSummaryNDetailsResponseDto getMyCreditSummaryNDetails(
      Long consumerId, String search, int page, int size) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    // 크레딧 거래내역 가져오기 (한 페이지 만큼)
    Page<CreditTradeInfoForSingleInquiryResponseDto> creditHistoriesPaged =
        getCreditHistoriesPaged(foundConsumer, search, page, size);

    List<CreditHistory> creditHistories = foundConsumer.getCreditHistoryList();
    // 크레딧 요약 정보 계산하기
    long[] summary = calcCreditSummary(creditHistories);

    return historyMapper.toCreditSummaryNDetailsResponseDto(
        foundConsumer.getAuctionCredit(), summary[0], summary[1], creditHistoriesPaged);
  }

  /**
   * 한 페이지만큼의 크레딧 내역 조회 (+필터링)
   *
   * @param consumer 현재 로그인 한 회원 객체
   * @param search 필터링 기준
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {Page<CreditTradeInfoForSingleInquiryResponseDto>} 한 페이지에 보여줄 크레딧 거래 내역
   */
  public Page<CreditTradeInfoForSingleInquiryResponseDto> getCreditHistoriesPaged(
      Consumer consumer, String search, int page, int size) {

    Pageable pageable = creditPaginationManager.getPageableByCreatedAt(page, size);

    Page<CreditHistory> creditHistoriesPaged = null;
    if ("charge".equals(search)) {
      creditHistoriesPaged =
          creditHistoryRepository.findByConsumerAndTradeCreditGreaterThan(consumer, 0L, pageable);
    } else if ("bid".equals(search)) {
      creditHistoriesPaged =
          creditHistoryRepository.findByConsumerAndTradeCreditLessThan(consumer, 0L, pageable);
    } else if (search == null) {
      creditHistoriesPaged = creditHistoryRepository.findByConsumer(consumer, pageable);
    }

    List<CreditTradeInfoForSingleInquiryResponseDto> histories =
        historyMapper.toCreditHistoriesPagedResponseDto(creditHistoriesPaged);

    int totalSize = creditHistoryRepository.findByConsumer(consumer).size();
    return creditPaginationManager.wrapByPage(histories, pageable, totalSize);
  }

  /**
   * 크레딧 결제 성공 시, 결제한 양만큼 크레딧 충전
   *
   * @param creditUpdateDto 회원 식별자 및 충전할 크레딧 정보
   */
  @Transactional
  public void updateConsumerCredit(CreditUpdateDto creditUpdateDto) {

    Consumer foundConsumer = consumerService.getConsumer(creditUpdateDto.getConsumerId());
    foundConsumer.assignAuctionCredit(
        foundConsumer.getAuctionCredit() + creditUpdateDto.getCredit());

    creditHistoryRepository.save(
        historyMapper.toCreditHistoryEntityByCharge(foundConsumer, creditUpdateDto.getCredit()));
  }

  /**
   * 크레딧 요약 정보 계산
   *
   * @param histories 로그인 한 회원의 모든 크레딧 거래 내역
   * @return {long[]} 첫번째 인자: 총 충전액, 두번째 인자: 총 사용액
   */
  public long[] calcCreditSummary(List<CreditHistory> histories) {

    long totalAcc = 0;
    long totalUse = 0;
    for (CreditHistory history : histories) {
      long tradeAmount = history.getTradeCredit();

      if (tradeAmount < 0) {
        totalUse = totalUse + Math.abs(tradeAmount);
      } else {
        totalAcc = totalAcc + tradeAmount;
      }
    }
    return new long[] {totalAcc, totalUse};
  }
}
