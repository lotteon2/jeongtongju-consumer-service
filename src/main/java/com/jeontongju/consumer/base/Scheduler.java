package com.jeontongju.consumer.base;

import com.jeontongju.consumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

  private final ConsumerService consumerService;

  @Scheduled(cron = "0 0 20 * * *")
  public void initPromotionCouponSetting() {
    log.info("Scheduler's issuePromotionCoupons executes..");
    consumerService.initPromotionCouponSetting();
  }
}
