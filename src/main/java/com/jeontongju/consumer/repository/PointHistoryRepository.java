package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.PointHistory;
import com.jeontongju.consumer.dto.temp.TradePathEnum;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

  Page<PointHistory> findAll(Pageable pageable);

  Page<PointHistory> findByConsumer(Consumer consumer, Pageable pageable);

  Page<PointHistory> findByConsumerOrderByCreatedAtDesc(Consumer consumer, Pageable pageable);

  List<PointHistory> findByConsumer(Consumer consumer);

  Page<PointHistory> findByConsumerAndTradePointLessThanOrderByCreatedAtDesc(
      Consumer consumer, Long tradePoint, Pageable pageable);

  Page<PointHistory> findByConsumerAndTradePointGreaterThanOrderByCreatedAtDesc(
      Consumer consumer, Long tradePoint, Pageable pageable);

  @Query(
      value =
          "SELECT ph FROM PointHistory ph WHERE ph.consumer = :consumer ORDER BY ph.createdAt DESC")
  List<PointHistory> findFirstByConsumerByCreatedAtDesc(
      @Param("consumer") Consumer consumer, Pageable pageable);

  @Query(
      value =
          "SELECT ph FROM PointHistory ph WHERE ph.consumer = :consumer AND ph.tradePathEnum = :tradePathEnum ORDER BY ph.createdAt DESC")
  List<PointHistory> findFirstByConsumerAndTradePathEnumByCreatedAtDesc(
      @Param("consumer") Consumer consumer,
      @Param("tradePathEnum") TradePathEnum tradePathEnum,
      Pageable pageable);
}
