package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.FeignFormat;
import com.jeontongju.consumer.service.ConsumerService;
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
  public FeignFormat<Object> createConsumerForSignup(
      @RequestBody ConsumerInfoForCreateRequestDto createRequestDto) {

    consumerService.createConsumerForSignup(createRequestDto);
    return FeignFormat.builder().code(HttpStatus.OK.value()).build();
  }

  @PostMapping("/consumers/oauth")
  public FeignFormat<Object> createConsumerForCreateBySns(
      @RequestBody ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {

    consumerService.createConsumerForCreateBySns(createBySnsRequestDto);
    return FeignFormat.builder().code(HttpStatus.OK.value()).build();
  }
}
