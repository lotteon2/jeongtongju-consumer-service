package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.domain.Subscription;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRespository extends JpaRepository<Subscription, Long> {

  Page<Subscription> findByConsumer(Consumer consumer, Pageable pageable);

  List<Subscription> findByConsumer(Consumer consumer);
}
