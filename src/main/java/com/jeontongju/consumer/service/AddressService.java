package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.request.AddressInfoForModifyRequestDto;
import com.jeontongju.consumer.dto.request.AddressInfoForRegisterRequestDto;
import com.jeontongju.consumer.dto.response.DefaultAddressInfoForInquiryResponseDto;
import com.jeontongju.consumer.exception.AddressNotFoundException;
import com.jeontongju.consumer.mapper.AddressMapper;
import com.jeontongju.consumer.repository.AddressRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import io.github.bitbox.bitbox.dto.AddressDto;
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
  private final AddressMapper addressMapper;

  /**
   * 주소지 단일 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @return {AddressInfoForSingleInquiryResponseDto} 해당 주소지 세부 정보
   */
  public DefaultAddressInfoForInquiryResponseDto getSingleAddressForInquiry(Long consumerId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    Address foundDefaultAddress =
        addressRepository
            .findByConsumerAndIsDefault(foundConsumer, true)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));

    return addressMapper.toDefaultAddressInquiryResponseDto(foundDefaultAddress);
  }

  /**
   * 주소지 목록 조회
   *
   * @param consumerId 로그인 한 회원 식별자
   * @return {List<AddressInfoForSingleInquiryResponseDto>} 해당 회원의 주소지 목록 (최대 5개)
   */
  public List<DefaultAddressInfoForInquiryResponseDto> getAddressesForListLookup(Long consumerId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    List<Address> addresses = foundConsumer.getAddressList();
    return addressMapper.toListLookupResponseDto(addresses);
  }

  /**
   * 경매 낙찰 시, 해당 회원 배송지 정보 가져오기
   *
   * @param consumerId 해당 회원 식별자
   * @return {AddressDto} 해당 회원의 배송지 정보
   */
  public AddressDto getConsumerAddress(Long consumerId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);
    Address foundDefaultAddress =
        addressRepository
            .findByConsumerAndIsDefault(foundConsumer, true)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));

    return addressMapper.toDefaultAddressDto(foundDefaultAddress);
  }

  /**
   * 주소지 추가
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param registerRequestDto 주소지 등록에 필요한 정보
   */
  @Transactional
  public void registerAddress(
      Long consumerId, AddressInfoForRegisterRequestDto registerRequestDto) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);

    List<Address> registeredAddresses = foundConsumer.getAddressList();

    // 최대 5개까지 주소지 등록 가능
    if (registeredAddresses.size() == 5) {
      deleteOldestAddress(foundConsumer);
    }

    if (registeredAddresses.isEmpty()) {
      addressRepository.save(addressMapper.toEntity(registerRequestDto, foundConsumer));
      return;
    }

    // 기본 주소지가 있다면 기본 주소지 해제
    if (registerRequestDto.getIsDefault() && !registeredAddresses.isEmpty()) {
      Address defaultAddress =
          addressRepository
              .findByConsumerAndIsDefault(foundConsumer, true)
              .orElseThrow(
                  () -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_DEFAULT_ADDRESS));
      defaultAddress.assignIsDefault(false);
    }

    addressRepository.save(addressMapper.toEntity(registerRequestDto, foundConsumer));
  }

  /**
   * 기본 주소지가 아닌 가장 오래된 주소지 삭제
   *
   * @param consumer 현재 로그인 한 회원 객체
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

  /**
   * 주소지 수정
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param addressId 변경할 주소지 식별자
   * @param modifyRequestDto 주소지 변경할 정보
   */
  @Transactional
  public void modifyAddress(
      Long consumerId, Long addressId, AddressInfoForModifyRequestDto modifyRequestDto) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);
    Address foundAddress =
        addressRepository
            .findByAddressId(addressId)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));

    if (modifyRequestDto.getIsDefault() && !foundConsumer.getAddressList().isEmpty()) {
      cancelOriginDefaultAddress(foundConsumer);
    }

    addressMapper.toRenewed(foundConsumer, foundAddress, modifyRequestDto);
  }

  /**
   * 기존 주소지를 일반 주소지로 해제
   *
   * @param consumer 현재 로그인 한 회원 객체
   */
  @Transactional
  public void cancelOriginDefaultAddress(Consumer consumer) {

    Address foundAddress =
        addressRepository
            .findByConsumerAndIsDefault(consumer, true)
            .orElseThrow(
                () -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_DEFAULT_ADDRESS));
    foundAddress.assignIsDefault(false);
  }

  /**
   * 기본 주소지 변경 요청이 있거나, 새로운 주소지가 기본 주소지로 추가되었을 때, 기본 주소지 변경
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param addressId 기본 주소지로 변경될 주소지 식별자
   */
  @Transactional
  public void changeDefaultAddress(Long consumerId, Long addressId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);
    cancelOriginDefaultAddress(foundConsumer);

    Address foundAddress =
        addressRepository
            .findByAddressId(addressId)
            .orElseThrow(() -> new AddressNotFoundException(CustomErrMessage.NOT_FOUND_ADDRESS));
    foundAddress.assignIsDefault(true);
  }

  /**
   * 해당 주소지 삭제
   *
   * @param consumerId 로그인 한 회원 식별자
   * @param addressId 삭제할 주소지 식별자
   */
  @Transactional
  public void deleteAddress(Long consumerId, Long addressId) {

    Consumer foundConsumer = consumerService.getConsumer(consumerId);
    addressRepository.deleteByConsumerAndAddressId(foundConsumer, addressId);
  }
}
