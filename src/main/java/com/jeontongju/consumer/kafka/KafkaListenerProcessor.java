package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerProcessor {
  private final ConsumerService consumerService;

  @KafkaListener(topics = KafkaTopicNameInfo.REDUCE_POINT)
  public void consumePoint(OrderInfoDto orderInfoDto) {

    try {
      log.info("KafkaListenerProcessor's consumePoint executes..");
      consumerService.consumePoint(orderInfoDto);
    } catch (Exception e) {
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }

  @KafkaListener(topics = "add-point")
  public void rollbackPoint(OrderInfoDto orderInfoDto) {

    try {
      log.info("KafkaListenerProcessor's rollbackPoint executes..");
      consumerService.rollbackPoint(orderInfoDto);
    } catch (Exception e) {
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }
}
