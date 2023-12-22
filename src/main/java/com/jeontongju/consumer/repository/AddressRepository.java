package com.jeontongju.consumer.repository;

import com.jeontongju.consumer.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressId(Long addressId);

    Optional<Address> findByIsDefault(boolean isDefault);
}
