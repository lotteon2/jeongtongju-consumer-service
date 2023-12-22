package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.domain.Consumer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressId(Long addressId);

    Optional<Address> findByConsumerAndIsDefault(Consumer consumer, boolean isDefault);

    void deleteByConsumerAndAddressId(Consumer foundConsumer, Long addressId);
}
