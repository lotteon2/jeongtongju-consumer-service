package com.jeontongju.consumer.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerProducer<T> {

  private final KafkaTemplate<String, T> kafkaTemplate;

  public void sendUpdateCoupon(String topicName, T data) {
    kafkaTemplate.send(topicName, data);
  }
}