package com.jeontongju.consumer.mapper;

import com.jeontongju.consumer.domain.Address;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import io.github.bitbox.bitbox.dto.AddressDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

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

  public AddressDto toDefaultAddressDto(Address address) {

    return AddressDto.builder()
        .basicAddress(address.getBasicAddress())
        .addressDetail(address.getAddressDetail())
        .recipientName(address.getRecipientName())
        .recipientPhoneNumber(address.getRecipientPhoneNumber())
        .zonecode(address.getZoneCode())
        .build();
  }
}
