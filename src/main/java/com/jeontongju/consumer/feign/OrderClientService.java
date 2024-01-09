package com.jeontongju.consumer.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderClientService {

  private final OrderServiceClient orderServiceClient;

  @Transactional
  public List<Long> getConsumerOrderIdsBySellerId(Long sellerId) {
    return orderServiceClient.getConsumerOrderIdsBySellerId(sellerId).getData();
  }
}
