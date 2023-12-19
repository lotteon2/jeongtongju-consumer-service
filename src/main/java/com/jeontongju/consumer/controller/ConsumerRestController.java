package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsumerRestController {

  private final ConsumerService consumerService;

  @GetMapping("/consumers")
  public ResponseEntity<ResponseFormat<ConsumerInfoForInquiryResponseDto>> getMyInfo(
      @RequestHeader Long memberId) {

    ConsumerInfoForInquiryResponseDto myInfoResponseDto = consumerService.getMyInfo(memberId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<ConsumerInfoForInquiryResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("소비자 개인정보 조회 성공")
                .data(myInfoResponseDto)
                .build());
  }
}
