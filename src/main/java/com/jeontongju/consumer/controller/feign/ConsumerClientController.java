package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.FeignFormat;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConsumerClientController {

  private final ConsumerService consumerService;

  @PostMapping("/consumers")
  public FeignFormat<Void> createConsumerForSignup(
      @RequestBody ConsumerInfoForCreateRequestDto createRequestDto) {

    consumerService.createConsumerForSignup(createRequestDto);
    return FeignFormat.<Void>builder().code(HttpStatus.OK.value()).build();
  }

  @PostMapping("/consumers/oauth")
  public FeignFormat<Void> createConsumerForCreateBySns(
      @RequestBody ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {

    consumerService.createConsumerForCreateBySns(createBySnsRequestDto);
    return FeignFormat.<Void>builder().code(HttpStatus.OK.value()).build();
  }

  @PostMapping("/point")
  public FeignFormat<Boolean> checkConsumerPoint(
      @RequestBody UserPointUpdateDto userPointUpdateDto) {

    consumerService.checkPoint(userPointUpdateDto);
    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).build();
  }

  @GetMapping("/consumers/{consumerId}/auction")
  public FeignFormat<ConsumerInfoForAuctionResponse> getConsumerInfoForAuction(
      @PathVariable Long consumerId) {

    return FeignFormat.<ConsumerInfoForAuctionResponse>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.getConsumerInfoForAuction(consumerId))
        .build();
  }

  @GetMapping("/consumers/{consumerId}/subscription")
  public FeignFormat<Boolean> getConsumerSubscription(@PathVariable Long consumerId) {

    return FeignFormat.<Boolean>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.getConsumerRegularPaymentInfo(consumerId))
        .build();
  }

  @PatchMapping("/consumers/{consumerId}/credit/{deductionCredit}")
  public FeignFormat<Boolean> consumeCreditByBidding(
      @PathVariable Long consumerId, @PathVariable Long deductionCredit) {

    consumerService.consumeCreditByBidding(consumerId, deductionCredit);
    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).build();
  }
}
