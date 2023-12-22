package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.CreditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
}
