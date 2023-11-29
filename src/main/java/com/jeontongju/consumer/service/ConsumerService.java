package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.CreateConsumerRequestDto;
import com.jeontongju.consumer.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerService {

  private final ConsumerRepository consumerRepository;

  public Consumer signUp(CreateConsumerRequestDto createConsumerDto) {
    return consumerRepository.save(Consumer.create(createConsumerDto));
  }
}
