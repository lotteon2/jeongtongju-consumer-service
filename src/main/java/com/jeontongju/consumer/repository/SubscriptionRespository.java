package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRespository extends JpaRepository<Subscription, Long> {

  Page<Subscription> findByConsumer(Consumer consumer, Pageable pageable);

  List<Subscription> findByConsumer(Consumer consumer);
}
