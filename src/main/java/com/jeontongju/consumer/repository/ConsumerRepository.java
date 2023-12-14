package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

  Optional<Consumer> findByEmail(String email);

  Optional<Consumer> findByConsumerId(Long consumerId);
}
