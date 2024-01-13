package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.CreditHistory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {

  Page<CreditHistory> findByConsumer(Consumer consumer, Pageable pageable);

  Page<CreditHistory> findByConsumerOrderByCreatedAtDesc(Consumer consumer, Pageable pageable);

  List<CreditHistory> findByConsumer(Consumer consumer);

  Page<CreditHistory> findByConsumerAndTradeCreditGreaterThanOrderByCreatedAtDesc(
      Consumer consumer, Long tradeCredit, Pageable pageable);

  Page<CreditHistory> findByConsumerAndTradeCreditLessThanOrderByCreatedAtDesc(
      Consumer consumer, long tradeCredit, Pageable pageable);
}
