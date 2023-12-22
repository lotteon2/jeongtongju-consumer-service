package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.request.AddressInfoForModifyRequestDto;
import com.jeontongju.consumer.dto.request.AddressInfoForRegisterRequestDto;
import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.service.AddressService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressRestController {

  private final AddressService addressService;

  @GetMapping("/consumers/addresses/{addressId}")
  public ResponseEntity<ResponseFormat<AddressInfoForSingleInquiryResponseDto>>
      getSingleAddressForInquiry(@PathVariable("addressId") Long addressId) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<AddressInfoForSingleInquiryResponseDto>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("주소지 단일 조회 성공")
                .data(addressService.getSingleAddressForInquiry(addressId))
                .build());
  }

  @GetMapping("/consumers/addresses")
  public ResponseEntity<ResponseFormat<List<AddressInfoForSingleInquiryResponseDto>>>
      getAddressesForListLookup(@RequestHeader Long memberId) {

    return ResponseEntity.ok()
        .body(
            ResponseFormat.<List<AddressInfoForSingleInquiryResponseDto>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("주소지 목록 조회 성공")
                .data(addressService.getAddressesForListLookup(memberId))
                .build());
  }

  @PostMapping("/consumers/addresses")
  public ResponseEntity<ResponseFormat<Void>> registerAddress(
      @RequestHeader Long memberId,
      @Valid @RequestBody AddressInfoForRegisterRequestDto registerRequestDto) {

    addressService.registerAddress(memberId, registerRequestDto);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("주소지 추가 성공")
                .build());
  }

  @PutMapping("/consumers/addresses/{addressId}")
  public ResponseEntity<ResponseFormat<Void>> modifyAddress(
      @RequestHeader Long memberId,
      @PathVariable("addressId") Long addressId,
      @Valid @RequestBody AddressInfoForModifyRequestDto modifyRequestDto) {

    addressService.modifyAddress(memberId, addressId, modifyRequestDto);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("주소지 수정 성공")
                .build());
  }

  @PatchMapping("/consumers/addresses/{addressId}/default")
  public ResponseEntity<ResponseFormat<Void>> changeDefaultAddress(
      @RequestHeader Long memberId, @PathVariable("addressId") Long addressId) {

    addressService.changeDefaultAddress(memberId, addressId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("기본 주소지 변경 성공")
                .build());
  }

  @DeleteMapping("/consumers/address/{addressId}")
  public ResponseEntity<ResponseFormat<Void>> deleteAddress(
      @RequestHeader Long memberId, @PathVariable("addressId") Long addressId) {

    addressService.deleteAddress(memberId, addressId);
    return ResponseEntity.ok()
        .body(
            ResponseFormat.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("주소지 삭제 성공")
                .build());
  }
}
