package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRespository extends JpaRepository<Subscription, Long> {
}
