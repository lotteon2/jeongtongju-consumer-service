package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListenerProcessor {
  private final String REDUCE_POINT = "reduce-point";
  private final ConsumerService consumerService;

  @KafkaListener(topics = REDUCE_POINT)
  public void consumePoint(OrderInfoDto orderInfoDto) {

    consumerService.consumePoint(orderInfoDto);
  }
}
