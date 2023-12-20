package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import org.springframework.stereotype.Component;

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

    List<AddressInfoForSingleInquiryResponseDto> addressesResponseDto = null;

    for (Address address : addresses) {
      addressesResponseDto.add(toSingleInquiryResponseDto(address));
    }
    return addressesResponseDto;
  }
}
