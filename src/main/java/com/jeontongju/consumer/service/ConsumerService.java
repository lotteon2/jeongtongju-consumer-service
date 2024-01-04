package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.request.ProfileImageUrlForModifyRequestDto;
import com.jeontongju.consumer.dto.response.*;
import com.jeontongju.consumer.dto.response.ConsumerInfoForInquiryResponseDto;
import com.jeontongju.consumer.dto.temp.*;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.exception.*;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.mapper.CouponMapper;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.repository.PointHistoryRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import com.jeontongju.consumer.utils.PaginationManager;
import io.github.bitbox.bitbox.dto.*;
import io.github.bitbox.bitbox.enums.MemberRoleEnum;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  private final CouponMapper couponMapper;
  private final PaginationManager paginationManager;

  private final KafkaTemplate<String, ConsumerRegularPaymentsCouponDto> kafkaTemplate;

  private static final Double POINT_ACC_RATE_NORMAL = 0.01;
  private static final Double POINT_ACC_RATE_YANGBAN = 0.03;

  /**
   * 소비자 회원 가입
   *
   * @param createRequestDto 회원 가입할 정보
   */
  @Transactional
  public void createConsumerForSignup(ConsumerInfoForCreateRequestDto createRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createRequestDto));
  }

  /**
   * 소비자 최초 소셜 로그인 시, 소비자 정보 저장 (식별자, 이메일, 프로필 이미지)
   *
   * @param createBySnsRequestDto 저장할 소비자 정보
   */
  @Transactional
  public void createConsumerForCreateBySns(
      ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createBySnsRequestDto));
  }

  /**
   * 계정 통합 시, 성인 인증으로 얻어온 정보 갱신(이름, 전화번호)
   *
   * @param accountConsolidationDto 성인 인증을 통해 얻어온 갱신 정보
   */
  @Transactional
  public void updateConsumerForAccountConsolidation(
      ConsumerInfoForAccountConsolidationDto accountConsolidationDto) {

    Consumer foundConsumer = getConsumer(accountConsolidationDto.getConsumerId());
    foundConsumer.assignName(accountConsolidationDto.getName());
    foundConsumer.assignPhoneNumber(accountConsolidationDto.getPhoneNumber());
    foundConsumer.approveAdult();
  }

  /**
   * 주문 및 결제 확정을 위한 포인트 차감
   *
   * @param orderInfoDto 주문 내역 정보
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
  }

  /**
   * 포인트 사용 정책 만족 여부 확인
   *
   * @param consumer 로그인 한 회원 객체
   * @param point 주문시 사용할 포인트
   * @param totalAmount 주문 총 금액
   */
  public void checkPointPolicy(Consumer consumer, Long point, Long totalAmount)
      throws PointInsufficientException, PointUsageOver10PercetageException {

    Long currentPoint = consumer.getPoint();
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
   * @param orderInfoDto 롤백할 주문 내역 정보
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
   * @param orderCancelDto 주문 취소 정보
   */
  @Transactional
  public void refundPointByCancelOrder(OrderCancelDto orderCancelDto) throws KafkaException {

    Consumer foundConsumer = getConsumer(orderCancelDto.getConsumerId());
    foundConsumer.assignPoint(foundConsumer.getPoint() + orderCancelDto.getPoint());
  }

  /**
   * 주문 취소 실패 시, 포인트 원상 복구
   *
   * @param orderCancelDto 주문 복구 정보
   */
  @Transactional
  public void recoverPointByFailedOrderCancel(OrderCancelDto orderCancelDto) {

    Consumer foundConsumer = getConsumer(orderCancelDto.getConsumerId());
    foundConsumer.assignPoint(foundConsumer.getPoint() - orderCancelDto.getPoint());
  }

  /**
   * 구독 결제 완료 후, 구독권 생성
   *
   * @param subscriptionDto 구독권 정보
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
   * @param userPointUpdateDto 회원 식별자 및 포인트 사용 확인에 필요한 정보
   */
  public void checkPoint(UserPointUpdateDto userPointUpdateDto) {

    Consumer foundConsumer = getConsumer(userPointUpdateDto.getConsumerId());
    checkPointPolicy(
        foundConsumer, userPointUpdateDto.getPoint(), userPointUpdateDto.getTotalAmount());
  }

  /**
   * 경매 입장 시, 소비자 개인 정보 확인 (이름, 프로필 이미지, 경매 크레딧)
   *
   * @param consumerId 경매에 입장할 소비자 식별자
   * @return {ConsumerInfoForAuctionResponse} 경매에 필요한 소비자 개인 정보
   */
  public ConsumerInfoForAuctionResponse getConsumerInfoForAuction(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return ConsumerInfoForAuctionResponse.toDto(foundConsumer);
  }

  /**
   * 해당 소비자 구독 결제 여부 확인 및 유효성 체크
   *
   * @param consumerId 구독 결제 여부 확인할 소비자 식별자
   * @return {boolean} 구독 결제 여부
   */
  public boolean getConsumerRegularPaymentInfo(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    //    boolean isRegularInfo = foundConsumer.getIsRegularPayment();
    //    boolean isExpired = false;
    //
    //    Optional<Subscription> latestSubscription =
    //        foundConsumer.getSubscriptionList().stream()
    //            .max(Comparator.comparing(Subscription::getEndDate));
    //
    //    if (latestSubscription.isPresent()
    //        && latestSubscription.get().getEndDate().isAfter(LocalDateTime.now())) {
    //      isExpired = true;
    //    }
    //
    //    return isRegularInfo || isExpired;
    return foundConsumer.getIsRegularPayment();
  }

  /**
   * 구독 해지
   *
   * @param consumerId 로그인 한 회원 식별자
   */
  @Transactional
  public void unsubscribe(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    if (!foundConsumer.getIsRegularPayment()) {
      throw new UnsubscribedConsumerException(CustomErrMessage.UNSUBSCRIBED_CONSUMER);
    }
    foundConsumer.unsubscribe();
  }

  /**
   * 경매 낙찰로 인한 크레딧 차감
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param deductionCredit 차감할 크레딧
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
   * @param consumerId 로그인 한 회원 식별자
   * @return {ConsumerInfoForInquiryResponseDto} 로그인 한 회원 정보
   */
  public ConsumerInfoForInquiryResponseDto getMyInfo(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toInquiryDto(foundConsumer);
  }

  /**
   * 포인트 및 크레딧 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @return {PointCreditForInquiryResponseDto} 포인트 + 크레딧 정보
   */
  public PointCreditForInquiryResponseDto getPointNCredit(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toPointCreditInquiryDto(foundConsumer);
  }

  /**
   * 구독 결제 내역 조회 (+페이징)
   *
   * @param consumerId 로그인 한 회원의 정보
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {Page<SubscriptionPaymentsInfoForInquiryResponseDto>}
   */
  public Page<SubscriptionPaymentsInfoForInquiryResponseDto> getMySubscriptionHistories(
      Long consumerId, int page, int size) {

    Consumer foundConsumer = getConsumer(consumerId);
    return subscriptionService.getSubscriptionHistories(foundConsumer, page, size);
  }

  /**
   * 내 정보 수정(프로필 이미지)
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param modifyRequestDto 수정할 개인 정보(프로필 이미지)
   */
  @Transactional
  public void modifyMyInfo(Long consumerId, ProfileImageUrlForModifyRequestDto modifyRequestDto) {

    Consumer foundConsumer = getConsumer(consumerId);
    foundConsumer.assignProfileImageUrl(modifyRequestDto.getProfileImageUrl());
  }

  /**
   * 로그인 후, 전역 상태 관리를 위한 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @return {MyInfoAfterSignInForResponseDto} 로그인 후 프론트 전역에 저장할 정보
   */
  public MyInfoAfterSignInForResponseDto getMyInfoAfterSignIn(Long consumerId) {

    return consumerMapper.toMyInfoDto(getConsumer(consumerId));
  }

  /**
   * 해당 회원의 이름 및 프로필 이미지 조회
   *
   * @param consumerId 회원 식별자
   * @return {NameImageForInquiryResponseDto} 회원의 이름 및 프로필 정보
   */
  public NameImageForInquiryResponseDto getNameNImageUrl(Long consumerId) {

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toNameImageDto(foundConsumer);
  }

  /**
   * 주문 확정시, 포인트 적립
   *
   * @param orderConfirmDto 주문 확정시 필요한 정보(소비자 식별자, 총 주문 금액)
   * @return {Long} 포인트 적립액
   */
  @Transactional
  public Long setAsidePointByOrderConfirm(OrderConfirmDto orderConfirmDto) {

    Consumer foundConsumer = getConsumer(orderConfirmDto.getConsumerId());

    Boolean isRegularPayment = foundConsumer.getIsRegularPayment();
    double pointAccRate = isRegularPayment ? POINT_ACC_RATE_YANGBAN : POINT_ACC_RATE_NORMAL;
    long accPoint = (long) Math.floor(orderConfirmDto.getProductAmount() * pointAccRate);

    foundConsumer.assignPoint(foundConsumer.getPoint() + accPoint);

    TradePathEnum tradePath =
        isRegularPayment ? TradePathEnum.YANGBAN_CONFIRMED : TradePathEnum.GENERAL_CONFIRMED;
    pointHistoryRepository.save(
        consumerMapper.toPointHistoryEntity(accPoint, tradePath, foundConsumer));
    return accPoint;
  }

  /**
   * 리뷰 작성 시 해당 회원의 포인트 적립 (with Kafka)
   *
   * @param pointUpdateDto 회원 및 적립 포인트 정보
   */
  @Transactional
  public void accPointByWritingReview(PointUpdateDto pointUpdateDto) {

    Consumer foundConsumer = getConsumer(pointUpdateDto.getConsumerId());
    foundConsumer.assignPoint(foundConsumer.getPoint() + pointUpdateDto.getPoint());
  }

  /**
   * (최초 소셜 로그인 후) 성인 인증 정보 갱신 (With OpenFeign)
   *
   * @param authInfoDto 성인인증으로 얻은 정보
   */
  @Transactional
  public void updateConsumerByAuth19(ImpAuthInfoForUpdateDto authInfoDto) {

    Consumer foundConsumer = getConsumer(authInfoDto.getConsumerId());
    foundConsumer.assignName(authInfoDto.getName());
    foundConsumer.assignPhoneNumber(authInfoDto.getPhoneNumber());
  }

  /**
   * 특정 회원 상세 조회
   *
   * @param consumerId 조회할 회원 식별자
   * @param memberRole 해당 작업을 수행할 회원의 역할(ROLE_ADMIN)
   * @return {SpecificConsumerDetailForInquiryResponseDto} 특정 회원 상세 정보
   */
  public SpecificConsumerDetailForInquiryResponseDto getConsumerDetailForInquiry(
      Long consumerId, MemberRoleEnum memberRole) {

    if (memberRole != MemberRoleEnum.ROLE_ADMIN) {
      throw new RuntimeException();
    }

    Consumer foundConsumer = getConsumer(consumerId);
    return consumerMapper.toSpecificConsumerDetailDto(foundConsumer);
  }

  /**
   * 모든 소비자 목록 조회 (탈퇴한 회원 포함)
   *
   * @param memberRole 해당 작업을 수행할 회원의 역할(ROLE_ADMIN)
   * @param page 페이징 첫 페이지 번호
   * @param size 페이지 당 보여줄 게시물 개수
   * @return {Page<ConsumerDetailForSingleInquiryResponseDto>} 모든 소비자 상세 정보
   */
  public Page<ConsumerDetailForSingleInquiryResponseDto> getMembersForListLookup(
      MemberRoleEnum memberRole, int page, int size) {

    if (memberRole != MemberRoleEnum.ROLE_ADMIN) {
      throw new NotAdminAccessDeniedException(CustomErrMessage.NOT_ADMIN_ACCESS_DENIED);
    }

    List<Consumer> foundAllConsumers = consumerRepository.findAll();
    List<ConsumerDetailForSingleInquiryResponseDto> allConsumersDto =
        consumerMapper.toAllConsumersDto(foundAllConsumers);

    Pageable pageable = paginationManager.getPageableByCreatedAt(page, size);
    return paginationManager.wrapByPage(allConsumersDto, pageable, foundAllConsumers.size());
  }

  /**
   * consumerId로 Consumer 찾기 (공통화)
   *
   * @param consumerId 회원의 식별자
   * @return {Consumer} 식별자로 찾은 소비자 객체
   */
  public Consumer getConsumer(Long consumerId) {

    return consumerRepository
        .findByConsumerId(consumerId)
        .orElseThrow(() -> new ConsumerNotFoundException(CustomErrMessage.NOT_FOUND_CONSUMER));
  }
}
