package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.response.AddressInfoForSingleInquiryResponseDto;
import com.jeontongju.consumer.service.AddressService;
import com.jeontongju.consumer.service.ConsumerService;
import io.github.bitbox.bitbox.dto.ResponseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsumerRestController {

  private final ConsumerService consumerService;
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
}
