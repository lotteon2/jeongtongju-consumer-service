package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

}
