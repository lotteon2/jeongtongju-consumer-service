package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.response.CreditTradeInfoForSummaryNDetailsResponseDto;
import com.jeontongju.consumer.dto.response.PointTradeInfoForSummaryNDetailsResponseDto;
import com.jeontongju.consumer.service.HistoryService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoryRestController {

  private final HistoryService historyService;

  @GetMapping("/consumers/point-history")
  public ResponseEntity<ResponseFormat<PointTradeInfoForSummaryNDetailsResponseDto>>
      getMyPointHistories(
          @RequestHeader Long memberId,
          @RequestParam(value = "search", required = false) String search,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size) {

    PointTradeInfoForSummaryNDetailsResponseDto myPointSummaryNDetails =
        historyService.getMyPointSummaryNDetails(memberId, search, page, size);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<PointTradeInfoForSummaryNDetailsResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("포인트 거래내역 조회 성공")
                .data(myPointSummaryNDetails)
                .build());
  }

  @GetMapping("/consumers/credit-history")
  public ResponseEntity<ResponseFormat<CreditTradeInfoForSummaryNDetailsResponseDto>>
      getMyCreditHistories(
          @RequestHeader Long memberId,
          @RequestParam(value = "search", required = false) String search,
          @RequestParam("page") int page,
          @RequestParam("size") int size) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<CreditTradeInfoForSummaryNDetailsResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("크레딧 내역 조회 성공")
                .data(historyService.getMyCreditSummaryNDetails(memberId, search, page, size))
                .build());
  }
}
