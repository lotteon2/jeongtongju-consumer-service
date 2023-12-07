package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateByKakaoRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

  private final ConsumerRepository consumerRepository;

  public void createConsumerForSignup(ConsumerInfoForCreateRequestDto createRequestDto) {
    consumerRepository.save(Consumer.create(createRequestDto));
  }

  public void createConsumerForCreateByKakao(
      ConsumerInfoForCreateByKakaoRequestDto createByKakaoRequestDto) {
    consumerRepository.save(Consumer.createByKakao(createByKakaoRequestDto));
  }
}
