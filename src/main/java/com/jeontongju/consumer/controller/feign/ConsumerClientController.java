package com.jeontongju.consumer.controller.feign;

import com.jeontongju.consumer.dto.ConsumerInfoForCreateByKakaoRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.SuccessFeignFormat;
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
  public SuccessFeignFormat<?> createConsumerForSignup(
      @RequestBody ConsumerInfoForCreateRequestDto createRequestDto) {

    consumerService.createConsumerForSignup(createRequestDto);
    return SuccessFeignFormat.builder().code(HttpStatus.OK.value()).build();
  }

  @PostMapping("/consumers/oauth")
  public SuccessFeignFormat<?> createConsumerForCreateByKakao(@RequestBody ConsumerInfoForCreateByKakaoRequestDto createByKakaoRequestDto) {

    consumerService.createConsumerForCreateByKakao(createByKakaoRequestDto);
    return SuccessFeignFormat.builder()
            .code(HttpStatus.OK.value())
            .build();
  }
}
