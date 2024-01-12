package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Consumer;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

  Optional<Consumer> findByEmail(String email);

  Optional<Consumer> findByConsumerId(Long consumerId);

  @Query(
      "SELECT FLOOR(c.age / 10) * 10 as ageGroup, COUNT(c) as total FROM Consumer c WHERE c.consumerId IN :consumerIds GROUP BY ageGroup")
  List<Object[]> findAgeGroupTotals(List<Long> consumerIds);

  @Query(
          "SELECT FLOOR(c.age / 10) * 10 as ageGroup, COUNT(c) as total FROM Consumer c GROUP BY ageGroup")
  List<Object[]> findAgeGroupTotalsByAllMember();
}
