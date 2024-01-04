package com.jeontongju.consumer.kafka;

import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.mapper.PaymentsMapper;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.service.HistoryService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.*;
import io.github.bitbox.bitbox.enums.NotificationTypeEnum;
import io.github.bitbox.bitbox.enums.RecipientTypeEnum;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerKafkaListener {

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

      consumerProducer.send(
          KafkaTopicNameInfo.SEND_ERROR_NOTIFICATION,
          ServerErrorForNotificationDto.builder()
              .recipientId(orderInfoDto.getUserPointUpdateDto().getConsumerId())
              .recipientType(RecipientTypeEnum.ROLE_CONSUMER)
              .notificationType(NotificationTypeEnum.INTERNAL_CONSUMER_SERVER_ERROR)
              .error(orderInfoDto)
              .build());
      //      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
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
      //      throw new KafkaDuringOrderException(CustomErrMessage.ERROR_KAFKA);
    }
  }

  /**
   * 주문 취소 실패 시, 포인트 차감 상태로 복구
   *
   * @param orderCancelDto 주문 복구 정보
   */
  @KafkaListener(topics = KafkaTopicNameInfo.RECOVER_CANCEL_ORDER_POINT)
  public void recoverPointByFailedOrderCancel(OrderCancelDto orderCancelDto) {
    try {
      consumerService.recoverPointByFailedOrderCancel(orderCancelDto);
      consumerProducer.send(KafkaTopicNameInfo.RECOVER_CANCEL_ORDER, orderCancelDto);
    } catch (Exception e) {
      log.error(
          "During Recover Order By Order Cancel Fail: Error while recovering points={}",
          e.getMessage());
      consumerProducer.send(
          KafkaTopicNameInfo.SEND_ERROR_CANCELING_ORDER_NOTIFICATION,
          MemberInfoForNotificationDto.builder()
              .recipientId(orderCancelDto.getConsumerId())
              .recipientType(RecipientTypeEnum.ROLE_CONSUMER)
              .notificationType(NotificationTypeEnum.INTERNAL_CONSUMER_SERVER_ERROR)
              .build());
    }
  }

  /**
   * 리뷰 작성 시 해당 회원의 포인트 적립
   *
   * @param pointUpdateDto 회원 및 적립 포인트 정보
   */
  @KafkaListener(topics = KafkaTopicNameInfo.UPDATE_REVIEW_POINT)
  public void accPointByWritingReview(PointUpdateDto pointUpdateDto) {

    try {
      consumerService.accPointByWritingReview(pointUpdateDto);
    } catch (Exception e) {
      log.error("[During Accumulate Point By Writing Review]: Error={}", e.getMessage());
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
