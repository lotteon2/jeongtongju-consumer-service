package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.service.HistoryService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.CreditUpdateDto;
import io.github.bitbox.bitbox.dto.KakaoPayCancelDto;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerProcessor {

  private final ConsumerService consumerService;
  private final HistoryService historyService;
  private final KafkaTemplate<String, KakaoPayCancelDto> kafkaTemplate;

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

  @KafkaListener(topics = KafkaTopicNameInfo.UPDATE_CREDIT)
  public void updateCredit(CreditUpdateDto creditUpdateDto){
    try{
      historyService.updateConsumerCredit(creditUpdateDto.getConsumerId(), creditUpdateDto.getCredit());
    }catch(Exception e){
      kafkaTemplate.send(KafkaTopicNameInfo.CANCEL_KAKAOPAY, KakaoPayCancelDto.builder().tid(creditUpdateDto.getTid())
                      .cancelAmount(creditUpdateDto.getCancelAmount()).cancelTaxFreeAmount(creditUpdateDto.getCancelTaxFreeAmount()).build());
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.CREATE_SUBSCRIPTION)
  public void createSubscription(SubscriptionDto subscriptionDto){
      try{
        consumerService.createSubscription(subscriptionDto);
      }catch(Exception e){
          kafkaTemplate.send(KafkaTopicNameInfo.CANCEL_KAKAOPAY, subscriptionDto.getSubscripton().cancel(subscriptionDto.getPaymentAmount(), subscriptionDto.getTaxFreeAmount()));
      }
  }
}
