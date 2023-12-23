package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.dto.response.*;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.exception.ConsumerNotFoundException;
import com.jeontongju.consumer.exception.InsufficientCreditException;
import com.jeontongju.consumer.exception.PointInsufficientException;
import com.jeontongju.consumer.exception.PointUsageOver10PercetageException;
import com.jeontongju.consumer.kafka.ConsumerProducer;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.ConsumerRegularPaymentsCouponDto;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.SubscriptionDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumerService {

  private final ConsumerRepository consumerRepository;
  private final SubscriptionService subscriptionService;
  private final ConsumerMapper consumerMapper;
  private final ConsumerProducer consumerProducer;
  private final KafkaTemplate<String, ConsumerRegularPaymentsCouponDto> kafkaTemplate;

  @Transactional
  public void createConsumerForSignup(ConsumerInfoForCreateRequestDto createRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createRequestDto));
  }

  @Transactional
  public void createConsumerForCreateBySns(
      ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createBySnsRequestDto));
  }

  /**
   * 주문 및 결제 확정을 위한 포인트 차감
   *
   * @param orderInfoDto
   */
  @Transactional
  public void consumePoint(OrderInfoDto orderInfoDto) throws KafkaException {

    log.info("ConsumerService's consumePoint executes..");
    UserPointUpdateDto userPointUpdateDto = orderInfoDto.getUserPointUpdateDto();

    if (userPointUpdateDto.getPoint() != null) {
      Consumer foundConsumer = getConsumer(userPointUpdateDto.getConsumerId());

      checkPointPolicy(
          foundConsumer, userPointUpdateDto.getPoint(), userPointUpdateDto.getTotalAmount());
      foundConsumer.consumePoint(foundConsumer.getPoint() - userPointUpdateDto.getPoint());
    }

    log.info("ConsumerService's consumePoint Successful executed!");
    consumerProducer.sendUpdateCoupon(KafkaTopicNameInfo.USE_COUPON, orderInfoDto);
  }

  /**
   * 포인트 사용 정책 만족 여부 확인
   *
   * @param foundConsumer
   * @param point
   * @param totalAmount
   */
  public void checkPointPolicy(Consumer foundConsumer, Long point, Long totalAmount)
      throws PointInsufficientException, PointUsageOver10PercetageException {

    Long currentPoint = foundConsumer.getPoint();
    if (currentPoint < point) { // 포인트 확인
      throw new PointInsufficientException(CustomErrMessage.INSUFFICIENT_POINT);
    }

    // 포인트 사용 정책 확인
    if (point > totalAmount * 0.1) {
      throw new PointUsageOver10PercetageException(CustomErrMessage.POINT_USAGE_OVER_10_PERCENTAGE);
    }
  }

  /**
   * 주문 및 결제 로직에서 에러 발생 시, 포인트 롤백
   *
   * @param orderInfoDto
   */
  @Transactional
  public void rollbackPoint(OrderInfoDto orderInfoDto) {

    UserPointUpdateDto userPointUpdateDto = orderInfoDto.getUserPointUpdateDto();
    if (userPointUpdateDto.getPoint() != null) {
      Consumer foundConsumer = getConsumer(userPointUpdateDto.getConsumerId());
      foundConsumer.rollbackPoint(foundConsumer.getPoint() + userPointUpdateDto.getPoint());
    }
  }

  @Transactional
  public void createSubscription(SubscriptionDto subscriptionDto) {
    Consumer foundConsumer = getConsumer(subscriptionDto.getConsumerId());
    foundConsumer.addSubscriptionInfo();

    subscriptionService.createSubscription(subscriptionDto, foundConsumer);
    kafkaTemplate.send(
        KafkaTopicNameInfo.ISSUE_REGULAR_PAYMENTS_COUPON,
        ConsumerRegularPaymentsCouponDto.builder()
            .consumerId(subscriptionDto.getConsumerId())
            .successedAt(subscriptionDto.getStartDate())
            .build());
  }

  public void hasPoint(UserPointUpdateDto userPointUpdateDto) {

    Consumer foundConsumer = getConsumer(userPointUpdateDto.getConsumerId());
    checkPointPolicy(
        foundConsumer, userPointUpdateDto.getPoint(), userPointUpdateDto.getTotalAmount());
  }

  public ConsumerInfoForAuctionResponse getConsumerInfoForAuction(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return ConsumerInfoForAuctionResponse.toDto(foundConsumer);
  }

  public boolean getConsumerRegularPaymentInfo(Long consumerId) {
    Consumer foundConsumer = getConsumer(consumerId);
    boolean isRegularInfo = foundConsumer.getIsRegularPayment();
    boolean isExpired = false;

    Optional<Subscription> latestSubscription =
        foundConsumer.getSubscriptionList().stream()
            .max(Comparator.comparing(Subscription::getEndDate));

    if (latestSubscription.isPresent()
        && latestSubscription.get().getEndDate().isAfter(LocalDateTime.now())) {
      isExpired = true;
    }

    return isRegularInfo || isExpired;
  }

  @Transactional
  public void consumeCreditByBidding(Long consumerId, Long deductionCredit) {

    Consumer consumer = getConsumer(consumerId);
    if (consumer.getAuctionCredit() < deductionCredit) {
      throw new InsufficientCreditException(CustomErrMessage.INSUFFICIENT_CREDIT);
    }

    consumer.assignAuctionCredit(consumer.getAuctionCredit() - deductionCredit);
  }

  public ConsumerInfoForInquiryResponseDto getMyInfo(Long memberId) {

    Consumer foundConsumer = getConsumer(memberId);
    return consumerMapper.toInquiryDto(foundConsumer);
  }

  public PointCreditForInquiryResponseDto getPointNCredit(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toPointCreditInquiryDto(foundConsumer);
  }

  public Page<SubscriptionPaymentsInfoForInquiryResponseDto> getMySubscriptionHistories(
      Long consumerId, int page, int size) {

    Consumer foundConsumer = getConsumer(consumerId);
    return subscriptionService.getSubscriptionHistories(foundConsumer, page, size);
  }

  /**
   * consumerId로 Consumer 찾기 (공통화)
   *
   * @param consumerId
   * @return Consumer
   */
  public Consumer getConsumer(Long consumerId) {

    return consumerRepository
        .findByConsumerId(consumerId)
        .orElseThrow(() -> new ConsumerNotFoundException(CustomErrMessage.NOT_FOUND_CONSUMER));
  }

  public MyInfoAfterSignInForResponseDto getMyInfoAfterSignIn(Long consumerId) {

    return consumerMapper.toMyInfoDto(getConsumer(consumerId));
  }
}
