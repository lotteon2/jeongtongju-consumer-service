package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
  Page<CreditHistory> findByConsumer(Consumer consumer, Pageable pageable);

  List<CreditHistory> findByConsumer(Consumer consumer);

  Page<CreditHistory> findByConsumerAndTradeCreditGreaterThan(
      Consumer consumer, Long tradeCredit, Pageable pageable);

  Page<CreditHistory> findByConsumerAndTradeCreditLessThan(
      Consumer consumer, long tradeCredit, Pageable pageable);
}
