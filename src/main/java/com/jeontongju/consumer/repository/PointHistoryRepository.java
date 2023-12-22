package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.PointHistory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    Page<PointHistory> findAll(Pageable pageable);

    Page<PointHistory> findByConsumer(Consumer consumer, Pageable pageable);

    List<PointHistory> findByConsumer(Consumer consumer);

    Page<PointHistory> findByConsumerAndTradePointLessThan(Consumer consumer, Long tradePoint, Pageable pageable);

    Page<PointHistory> findByConsumerAndTradePointGreaterThan(Consumer consumer, Long tradePoint, Pageable pageable);
}
