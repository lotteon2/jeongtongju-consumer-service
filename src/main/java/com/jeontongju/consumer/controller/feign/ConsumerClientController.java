package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;

import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.FeignFormat;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    Boolean hasPoint = consumerService.checkConsumerPoint(userPointUpdateDto);

    return FeignFormat.<Boolean>builder().code(HttpStatus.OK.value()).data(hasPoint).build();
  }
}
