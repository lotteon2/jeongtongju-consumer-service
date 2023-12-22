package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.request.AddressInfoForRegisterRequestDto;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.exception.AddressNotFoundException;
import com.jeontongju.consumer.exception.ConsumerNotFoundException;
import com.jeontongju.consumer.mapper.AddressMapper;
import com.jeontongju.consumer.repository.AddressRepository;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;
  private final ConsumerService consumerService;
  private final ConsumerRepository consumerRepository;
  private final AddressMapper addressMapper;

  /**
   * 주소지 단일 조회
   *
   * @param addressId
   * @return
   */
  public AddressInfoForSingleInquiryResponseDto getSingleAddressForInquiry(Long addressId) {

    Address foundAddress =
        addressRepository
            .findByAddressId(addressId)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));
    return addressMapper.toSingleInquiryResponseDto(foundAddress);
  }

  /**
   * 주소지 목록 조회
   *
   * @param consumerId
   * @return
   */
  public List<AddressInfoForSingleInquiryResponseDto> getAddressesForListLookup(Long consumerId) {

    Consumer foundConsumer =
        consumerRepository
            .findByConsumerId(consumerId)
            .orElseThrow(() -> new ConsumerNotFoundException(CustomErrMessage.NOT_FOUND_CONSUMER));

    List<Address> addresses = foundConsumer.getAddressList();
    return addressMapper.toListLookupResponseDto(addresses);
  }

  /**
   * 주소지 추가
   *
   * @param consumerId
   * @param registerRequestDto
   */
  @Transactional
  public void registerAddress(
      Long consumerId, AddressInfoForRegisterRequestDto registerRequestDto) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    List<Address> registerdAddresses = foundConsumer.getAddressList();
    // 최대 5개까지 주소지 등록 가능
    if (registerdAddresses.size() == 5) {
      deleteOldestAddress(foundConsumer);
    }

    // 기본 주소지가 있다면 기본 주소지 해제
    if (registerRequestDto.getIsDefault() && !registerdAddresses.isEmpty()) {
      Address defaultAddress =
          addressRepository
              .findByIsDefault(true)
              .orElseThrow(
                  () -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_DEFAULT_ADDRESS));
      defaultAddress.assignIsDefault(false);
    }

    addressRepository.save(addressMapper.toEntity(registerRequestDto, foundConsumer));
  }

  /**
   * 기본주소지가 아닌 가장 오래된 주소지 삭제
   *
   * @param consumer
   */
  @Transactional
  public void deleteOldestAddress(Consumer consumer) {

    List<Address> addressList = consumer.getAddressList();
    Collections.sort(addressList, Comparator.comparingLong(address -> address.getAddressId()));

    Address oldestAddress = addressList.get(0);
    Long deleteId =
        oldestAddress.getIsDefault()
            ? addressList.get(1).getAddressId()
            : oldestAddress.getAddressId();
    addressRepository.deleteById(deleteId);
  }
}
