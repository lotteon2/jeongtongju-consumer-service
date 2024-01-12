package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.temp.*;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.FeignFormat;
import com.jeontongju.consumer.service.AddressService;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.*;
import io.github.bitbox.bitbox.dto.ConsumerInfoForCreateRequestDto;
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

  @PutMapping("/consumers/account-consolidation")
  public FeignFormat<Void> updateConsumerForAccountConsolidation(
      @RequestBody ConsumerInfoForAccountConsolidationDto accountConsolidationDto) {

    consumerService.updateConsumerForAccountConsolidation(accountConsolidationDto);
    return FeignFormat.<Void>builder().code(HttpStatus.OK.value()).build();
  }

  @PutMapping("/consumers/adult-certification")
  public FeignFormat<Void> updateConsumerByAuth19(
      @RequestBody ImpAuthInfoForUpdateDto authInfoDto) {

    consumerService.updateConsumerByAuth19(authInfoDto);
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
  public FeignFormat<AddressDto> getConsumerAddress(@PathVariable Long consumerId) {

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

    consumerService.checkPoint(userPointUpdateDto);
    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).build();
  }

  @GetMapping("/consumers/{consumerId}/auction")
  public FeignFormat<ConsumerInfoDto> getConsumerInfoForAuction(
      @PathVariable Long consumerId) {

    return FeignFormat.<ConsumerInfoDto>builder()
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

  @PostMapping("/orders-confirm")
  public FeignFormat<Long> setAsidePointByOrderConfirm(
      @RequestBody OrderConfirmDto orderConfirmDto) {

    return FeignFormat.<Long>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.setAsidePointByOrderConfirm(orderConfirmDto))
        .build();
  }

  @GetMapping("/consumers/age-distribution")
  public FeignFormat<AgeDistributionForShowResponseDto> getAgeDistributionForAllMembers() {

    return FeignFormat.<AgeDistributionForShowResponseDto>builder()
        .code(HttpStatus.OK.value())
        .data(consumerService.getAgeDistributionForAllMembers())
        .build();
  }
}
