package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.exception.ConsumerNotFoundException;
import com.jeontongju.consumer.exception.KafkaDuringOrderException;
import com.jeontongju.consumer.kafka.ConsumerProducer;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import com.jeontongju.consumer.exception.InsufficientCreditException;
import io.github.bitbox.bitbox.dto.OrderInfoDto;
import io.github.bitbox.bitbox.dto.UserPointUpdateDto;
import io.github.bitbox.bitbox.util.KafkaTopicNameInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumerService {

  private final ConsumerRepository consumerRepository;
  private final ConsumerMapper consumerMapper;
  private final ConsumerProducer consumerProducer;

  @Transactional
  public void createConsumerForSignup(ConsumerInfoForCreateRequestDto createRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createRequestDto));
  }

  @Transactional
  public void createConsumerForCreateBySns(
      ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createBySnsRequestDto));
  }

  @Transactional
  public void consumePoint(OrderInfoDto orderInfoDto) {

    UserPointUpdateDto userPointUpdateDto = orderInfoDto.getUserPointUpdateDto();
    Consumer foundConsumer =
        consumerRepository.findByConsumerId(userPointUpdateDto.getConsumerId()).orElseThrow();
    foundConsumer.consumePoint(userPointUpdateDto.getPoint());

    consumerProducer.sendUpdateCoupon(KafkaTopicNameInfo.USE_COUPON, orderInfoDto);
  }

  public Boolean checkConsumerPoint(UserPointUpdateDto userPointUpdateDto) {

    Consumer foundConsumer =
        consumerRepository
            .findByConsumerId(userPointUpdateDto.getConsumerId())
            .orElseThrow(() -> new ConsumerNotFoundException(CustomErrMessage.NOT_FOUND_CONSUMER));

    return foundConsumer.getPoint() >= userPointUpdateDto.getPoint();
  }
  
  public ConsumerInfoForAuctionResponse getConsumerInfoForAuction(Long consumerId) {
    Consumer foundConsumer =
        consumerRepository
            .findByConsumerId(consumerId)
            .orElseThrow(() -> new EntityNotFoundException(""));

    return ConsumerInfoForAuctionResponse.toDto(foundConsumer);
  }

  @Transactional
  public void consumeCreditByBidding(Long consumerId, Long deductionCredit) {
    Consumer consumer =
        consumerRepository
            .findByConsumerId(consumerId)
            .orElseThrow(() -> new EntityNotFoundException(CustomErrMessage.NOT_FOUND_CONSUMER));
    if (consumer.getAuctionCredit() < deductionCredit) {
      throw new InsufficientCreditException(CustomErrMessage.INSUFFICIENT_CREDIT);
    }

    consumer.assignAuctionCredit(consumer.getAuctionCredit() - deductionCredit);
  }
}
