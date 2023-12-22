package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.request.AddressInfoForModifyRequestDto;
import com.jeontongju.consumer.dto.request.AddressInfoForRegisterRequestDto;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {

  public AddressInfoForSingleInquiryResponseDto toSingleInquiryResponseDto(Address address) {

    return AddressInfoForSingleInquiryResponseDto.builder()
        .addressId(address.getAddressId())
        .basicAddress(address.getBasicAddress())
        .addressDetail(address.getAddressDetail())
        .zonecode(address.getZoneCode())
        .recipientName(address.getRecipientName())
        .recipientPhoneNumber(address.getRecipientPhoneNumber())
        .isDefault(address.getIsDefault())
        .build();
  }

  public List<AddressInfoForSingleInquiryResponseDto> toListLookupResponseDto(
      List<Address> addresses) {

    List<AddressInfoForSingleInquiryResponseDto> addressesResponseDto = new ArrayList<>();

    for (Address address : addresses) {
      addressesResponseDto.add(toSingleInquiryResponseDto(address));
    }
    return addressesResponseDto;
  }

  public Address toEntity(AddressInfoForRegisterRequestDto registerRequestDto, Consumer consumer) {

    Boolean isDefault = consumer.getAddressList().isEmpty() || registerRequestDto.getIsDefault();

    return Address.builder()
        .basicAddress(registerRequestDto.getBasicAddress())
        .addressDetail(registerRequestDto.getAddressDetail())
        .zoneCode(registerRequestDto.getZonecode())
        .recipientName(registerRequestDto.getRecipientName())
        .recipientPhoneNumber(registerRequestDto.getRecipientPhoneNumber())
        .isDefault(isDefault)
        .consumer(consumer)
        .build();
  }

  public void toRenewed(Consumer consumer, Address address, AddressInfoForModifyRequestDto modifyRequestDto) {

    address.assignBasicAddress(modifyRequestDto.getBasicAddress());
    address.assignAddressDetail(modifyRequestDto.getAddressDetail());
    address.assignZonecone(modifyRequestDto.getZonecode());
    address.assignRecipientName(modifyRequestDto.getRecipientName());
    address.assignRecipientPhoneNumber(modifyRequestDto.getRecipientPhoneNumber());
    address.assignIsDefault(modifyRequestDto.getIsDefault());
  }
}
