package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.request.ProfileImageUrlForModifyRequestDto;
import com.jeontongju.consumer.dto.response.*;
import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import io.github.bitbox.bitbox.enums.MemberRoleEnum;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @GetMapping("/consumers-list")
  public ResponseEntity<ResponseFormat<Page<ConsumerDetailForSingleInquiryResponseDto>>>
      getMembersForListLookup(
          @RequestHeader MemberRoleEnum memberRole,
          @RequestParam int page,
          @RequestParam int size) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<ConsumerDetailForSingleInquiryResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("회원 목록 조회 성공")
                .data(consumerService.getMembersForListLookup(memberRole, page, size))
                .build());
  }

  @PatchMapping("/consumers")
  public ResponseEntity<ResponseFormat<Void>> modifyMyInfo(
      @RequestHeader Long memberId,
      @Valid @RequestBody ProfileImageUrlForModifyRequestDto modifyRequestDto) {

    consumerService.modifyMyInfo(memberId, modifyRequestDto);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("소비자 개인정보 수정 성공")
                .build());
  }

  @GetMapping("/consumers/{consumerId}")
  public ResponseEntity<ResponseFormat<SpecificConsumerDetailForInquiryResponseDto>>
      getConsumerDetailForInquiry(
          @PathVariable Long consumerId, @RequestHeader MemberRoleEnum memberRole) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<SpecificConsumerDetailForInquiryResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("회원 상세 조회 성공")
                .data(consumerService.getConsumerDetailForInquiry(consumerId, memberRole))
                .build());
  }

  @GetMapping("/consumers/point-credit")
  public ResponseEntity<ResponseFormat<PointCreditForInquiryResponseDto>> getMyPointNCredit(
      @RequestHeader Long memberId) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<PointCreditForInquiryResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("포인트 + 크레딧 조회 성공")
                .data(consumerService.getPointNCredit(memberId))
                .build());
  }

  @GetMapping("/consumers/subscription-history")
  public ResponseEntity<ResponseFormat<Page<SubscriptionPaymentsInfoForInquiryResponseDto>>>
      getMySubscriptionHistories(
          @RequestHeader Long memberId,
          @RequestParam(value = "page", defaultValue = "0") int page,
          @RequestParam(value = "size", defaultValue = "10") int size) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Page<SubscriptionPaymentsInfoForInquiryResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("구독 결제 내역 조회 성공")
                .data(consumerService.getMySubscriptionHistories(memberId, page, size))
                .build());
  }

  @GetMapping("/consumers/my-info")
  public ResponseEntity<ResponseFormat<MyInfoAfterSignInForResponseDto>> getMyInfoAfterSignIn(
      @RequestHeader Long memberId) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<MyInfoAfterSignInForResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("로그인 직후, 내 정보 조회 성공")
                .data(consumerService.getMyInfoAfterSignIn(memberId))
                .build());
  }

  @DeleteMapping("/consumers/subscription")
  public ResponseEntity<ResponseFormat<Void>> unsubscribe(@RequestHeader Long memberId) {

    consumerService.unsubscribe(memberId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("구독 해지 성공")
                .build());
  }
}
