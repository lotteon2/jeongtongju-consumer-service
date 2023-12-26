package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.request.ProfileImageUrlForModifyRequestDto;
import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.domain.Subscription;
import com.jeontongju.consumer.dto.response.*;
import com.jeontongju.consumer.dto.temp.*;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.exception.ConsumerNotFoundException;
import com.jeontongju.consumer.exception.InsufficientCreditException;
import com.jeontongju.consumer.exception.PointInsufficientException;
import com.jeontongju.consumer.exception.PointUsageOver10PercetageException;
import com.jeontongju.consumer.kafka.ConsumerProducer;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.mapper.CouponMapper;
import com.jeontongju.consumer.mapper.HistoryMapper;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.repository.PointHistoryRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.*;
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
  private final PointHistoryRepository pointHistoryRepository;
  private final SubscriptionService subscriptionService;
  private final ConsumerMapper consumerMapper;
  private final HistoryMapper historyMapper;
  private final CouponMapper couponMapper;

  private final ConsumerProducer consumerProducer;
  private final KafkaTemplate<String, ConsumerRegularPaymentsCouponDto> kafkaTemplate;

  private static final Double POINT_ACC_RATE_NORMAL = 0.01;
  private static final Double POINT_ACC_RATE_YANGBAN = 0.03;

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
    consumerProducer.send(KafkaTopicNameInfo.USE_COUPON, orderInfoDto);
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

  /**
   * 주문 취소 시, 포인트 환불 처리
   *
   * @param orderCancelDto
   */
  @Transactional
  public void refundPointByCancelOrder(OrderCancelDto orderCancelDto) throws KafkaException {

    Consumer foundConsumer = getConsumer(orderCancelDto.getConsumerId());
    foundConsumer.assignPoint(foundConsumer.getPoint() + orderCancelDto.getPoint());
    consumerProducer.send(KafkaTopicNameInfo.CANCEL_ORDER_COUPON, orderCancelDto);
  }

  /**
   * 구독 결제 완료 후, 구독권 생성
   *
   * @param subscriptionDto
   */
  @Transactional
  public void createSubscription(SubscriptionDto subscriptionDto) {
    Consumer foundConsumer = getConsumer(subscriptionDto.getConsumerId());
    foundConsumer.addSubscriptionInfo();

    subscriptionService.createSubscription(subscriptionDto, foundConsumer);
    kafkaTemplate.send(
        KafkaTopicNameInfo.ISSUE_REGULAR_PAYMENTS_COUPON,
        couponMapper.toConsumerRegularPaymentsCouponDto(subscriptionDto));
  }

  /**
   * 주문에 들어가기 전, 주문에 필요한 포인트 소유 여부 확인
   *
   * @param userPointUpdateDto
   */
  public void checkPoint(UserPointUpdateDto userPointUpdateDto) {

    Consumer foundConsumer = getConsumer(userPointUpdateDto.getConsumerId());
    checkPointPolicy(
        foundConsumer, userPointUpdateDto.getPoint(), userPointUpdateDto.getTotalAmount());
  }

  /**
   * 경매 입장 시, 소비자 개인 정보 확인 (이름, 프로필 이미지, 경매크레딧)
   *
   * @param consumerId
   * @return ConsumerInfoForAuctionResponse
   */
  public ConsumerInfoForAuctionResponse getConsumerInfoForAuction(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return ConsumerInfoForAuctionResponse.toDto(foundConsumer);
  }

  /**
   * 해당 소비자 구독 결제 여부 확인 및 유효성 체크
   *
   * @param consumerId
   * @return boolean
   */
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

  /**
   * 경매 낙찰로 인한 크레딧 차감
   *
   * @param consumerId
   * @param deductionCredit
   */
  @Transactional
  public void consumeCreditByBidding(Long consumerId, Long deductionCredit) {

    Consumer consumer = getConsumer(consumerId);
    if (consumer.getAuctionCredit() < deductionCredit) {
      throw new InsufficientCreditException(CustomErrMessage.INSUFFICIENT_CREDIT);
    }

    consumer.assignAuctionCredit(consumer.getAuctionCredit() - deductionCredit);
  }

  /**
   * 내 정보 조회
   *
   * @param consumerId
   * @return ConsumerInfoForInquiryResponseDto
   */
  public ConsumerInfoForInquiryResponseDto getMyInfo(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
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
   * 내 정보 수정(프로필 이미지)
   *
   * @param consumerId
   * @param modifyRequestDto
   */
  @Transactional
  public void modifyMyInfo(Long consumerId, ProfileImageUrlForModifyRequestDto modifyRequestDto) {

    Consumer foundConsumer = getConsumer(consumerId);
    foundConsumer.assignProfileImageUrl(modifyRequestDto.getProfileImageUrl());
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

  public NameImageForInquiryResponseDto getNameNImageUrl(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toNameImageDto(foundConsumer);
  }

  @Transactional
  public Long setAsidePointByOrderConfirm(OrderConfirmDto orderConfirmDto) {

    Consumer foundConsumer = getConsumer(orderConfirmDto.getConsumerId());

    Boolean isRegularPayment = foundConsumer.getIsRegularPayment();
    double pointAccRate =
        isRegularPayment ? POINT_ACC_RATE_YANGBAN : POINT_ACC_RATE_NORMAL;
    long accPoint = (long) Math.floor(orderConfirmDto.getProductAmount() * pointAccRate);

    foundConsumer.assignPoint(foundConsumer.getPoint() + accPoint);

    TradePathEnum tradePath = isRegularPayment ? TradePathEnum.YANGBAN_CONFIRMED : TradePathEnum.GENERAL_CONFIRMED;
    pointHistoryRepository.save(consumerMapper.toPointHistoryEntity(accPoint, tradePath, foundConsumer));
    return accPoint;
  }
}
