package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.dto.temp.FeignFormat;
import com.jeontongju.consumer.service.ConsumerService;
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

  @GetMapping("/consumers/{consumerId}/auction")
  public FeignFormat<ConsumerInfoForAuctionResponse> getConsumerInfoForAuction(
      @PathVariable Long consumerId) {

    ConsumerInfoForAuctionResponse consumerInfoForAuction =
        consumerService.getConsumerInfoForAuction(consumerId);

    return FeignFormat.<ConsumerInfoForAuctionResponse>builder()
        .code(HttpStatus.OK.value())
        .data(consumerInfoForAuction)
        .build();
  }

  @PatchMapping("/consumers/{consumerId}/credit/{deductionCredit}")
  public FeignFormat<Boolean> consumeCreditByBidding(
      @PathVariable Long consumerId, @PathVariable Long deductionCredit) {

    consumerService.consumeCreditByBidding(consumerId, deductionCredit);
    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).build();
  }
}
