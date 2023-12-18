package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListenerProcessor {
  private final String REDUCE_POINT = "reduce-point";
  private final ConsumerService consumerService;

  @KafkaListener(topics = REDUCE_POINT)
  public void consumePoint(OrderInfoDto orderInfoDto) {
    try {
      consumerService.consumePoint(orderInfoDto);
    } catch (KafkaException e) {
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }
}
