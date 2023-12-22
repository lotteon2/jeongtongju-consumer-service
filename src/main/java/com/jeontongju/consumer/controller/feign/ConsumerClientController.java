package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.temp.*;
import com.jeontongju.consumer.service.AddressService;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.AddressDto;
import io.github.bitbox.bitbox.dto.OrderConfirmDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConsumerClientController {

  private final ConsumerService consumerService;
  private final AddressService addressService;

  @PostMapping("/consumers")
  public FeignFormat<Void> createConsumerForSignup(
      @RequestBody ConsumerInfoForCreateRequestDto createRequestDto) {

    consumerService.createConsumerForSignup(createRequestDto);
    return FeignFormat.<Void>builder().code(HttpStatus.OK.value()).build();
  }

  @GetMapping("/consumers/{consumerId}/name-image")
  public FeignFormat<NameImageForInquiryResponseDto> getNameNImageUrl(
      @PathVariable("consumerId") Long consumerId) {

    return FeignFormat.<NameImageForInquiryResponseDto>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.getNameNImageUrl(consumerId))
        .build();
  }

  @GetMapping("/consumers/{consumerId}/address")
  public FeignFormat<AddressDto> getConsumerAddress(@PathVariable("consumerId") Long consumerId) {

    return FeignFormat.<AddressDto>builder()
        .code(HttpStatus.OK.value())
        .data(addressService.getConsumerAddress(consumerId))
        .build();
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

    consumerService.hasPoint(userPointUpdateDto);
    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).build();
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

  @PostMapping("/orders-confirm")
  public FeignFormat<Long> setAsidePointByOrderConfirm(
      @RequestBody OrderConfirmDto orderConfirmDto) {

    return FeignFormat.<Long>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.setAsidePointByOrderConfirm(orderConfirmDto))
        .build();
  }
}
