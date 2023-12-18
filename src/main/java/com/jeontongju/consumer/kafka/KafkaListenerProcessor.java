package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListenerProcessor {
  private final ConsumerService consumerService;

  @KafkaListener(topics = KafkaTopicNameInfo.REDUCE_POINT)
  public void consumePoint(OrderInfoDto orderInfoDto) {

    try {
      consumerService.consumePoint(orderInfoDto);
    } catch (KafkaException e) {
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }
}
