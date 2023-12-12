package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateBySnsRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import com.jeontongju.consumer.dto.temp.ConsumerInfoForAuctionResponse;
import com.jeontongju.consumer.exception.InsufficientCreditException;
import com.jeontongju.consumer.mapper.ConsumerMapper;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsumerService {

  private final ConsumerRepository consumerRepository;
  private final ConsumerMapper consumerMapper;

  @Transactional
  public void createConsumerForSignup(ConsumerInfoForCreateRequestDto createRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createRequestDto));
  }

  @Transactional
  public void createConsumerForCreateBySns(
      ConsumerInfoForCreateBySnsRequestDto createBySnsRequestDto) {
    consumerRepository.save(consumerMapper.toEntity(createBySnsRequestDto));
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
