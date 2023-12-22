package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.exception.AddressNotFoundException;
import com.jeontongju.consumer.mapper.AddressMapper;
import com.jeontongju.consumer.repository.AddressRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.AddressDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;
  private final ConsumerService consumerService;
  private final AddressMapper addressMapper;

  public AddressInfoForSingleInquiryResponseDto getSingleAddressForInquiry(Long addressId) {

    Address foundAddress =
        addressRepository
            .findByAddressId(addressId)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));
    return addressMapper.toSingleInquiryResponseDto(foundAddress);
  }

  public List<AddressInfoForSingleInquiryResponseDto> getAddressesForListLookup(Long consumerId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    List<Address> addresses = foundConsumer.getAddressList();
    return addressMapper.toListLookupResponseDto(addresses);
  }

  public AddressDto getConsumerAddress(Long consumerId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);
    Address foundDefaultAddress =
        addressRepository
            .findByConsumerAndIsDefault(foundConsumer, true)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));

    return addressMapper.toDefaultAddressDto(foundDefaultAddress);
  }
}
