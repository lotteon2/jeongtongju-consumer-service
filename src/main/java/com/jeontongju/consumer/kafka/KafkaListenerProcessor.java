package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.mapper.PaymentsMapper;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.service.HistoryService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.*;
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
  private final PaymentsMapper paymentsMapper;
  private final ConsumerProducer consumerProducer;
  private final KafkaTemplate<String, KakaoPayCancelDto> kafkaTemplate;

  @KafkaListener(topics = KafkaTopicNameInfo.REDUCE_POINT)
  public void consumePoint(OrderInfoDto orderInfoDto) {

    try {
      consumerService.consumePoint(orderInfoDto);
      if (orderInfoDto.getUserCouponUpdateDto().getCouponCode() == null) {
        consumerProducer.send(KafkaTopicNameInfo.REDUCE_STOCK, orderInfoDto);
      } else {
        consumerProducer.send(KafkaTopicNameInfo.USE_COUPON, orderInfoDto);
      }
    } catch (Exception e) {
      log.error("During Order Process: Error while consume points={}", e.getMessage());
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.ADD_POINT)
  public void rollbackPoint(OrderInfoDto orderInfoDto) {

    try {
      consumerService.rollbackPoint(orderInfoDto);
    } catch (Exception e) {
      log.error("During Rollback Points: Error while add points={}", e.getMessage());
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.CANCEL_ORDER_POINT)
  public void refundPointByCancelOrder(OrderCancelDto orderCancelDto) {

    try {
      consumerService.refundPointByCancelOrder(orderCancelDto);
      if (orderCancelDto.getCouponCode() == null) {
        consumerProducer.send(KafkaTopicNameInfo.CANCEL_ORDER_PAYMENT, orderCancelDto);
      } else {
        consumerProducer.send(KafkaTopicNameInfo.CANCEL_ORDER_COUPON, orderCancelDto);
      }
    } catch (Exception e) {
      log.error("During Cancel Order: Error while refunding points={}", e.getMessage());
      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.UPDATE_CREDIT)
  public void updateCredit(CreditUpdateDto creditUpdateDto) {

    try {
      historyService.updateConsumerCredit(
          creditUpdateDto.getConsumerId(), creditUpdateDto.getCredit());
    } catch (Exception e) {
      log.error("During Charge Credit: Error while update credits={}", e.getMessage());
      kafkaTemplate.send(
          KafkaTopicNameInfo.CANCEL_KAKAOPAY, paymentsMapper.toKakaoPayCancelDto(creditUpdateDto));
    }
  }

  @KafkaListener(topics = KafkaTopicNameInfo.CREATE_SUBSCRIPTION)
  public void createSubscription(SubscriptionDto subscriptionDto) {

    try {
      consumerService.createSubscription(subscriptionDto);
    } catch (Exception e) {
      log.error("During Subscription Payments: Error while create subscription={}", e.getMessage());
      kafkaTemplate.send(
          KafkaTopicNameInfo.CANCEL_KAKAOPAY,
          subscriptionDto
              .getSubscripton()
              .cancel(subscriptionDto.getPaymentAmount(), subscriptionDto.getTaxFreeAmount()));
    }
  }
}
