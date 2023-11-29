package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.CodeInfoForAuthRequestDto;
import com.jeontongju.consumer.dto.CreateConsumerRequestDto;
import com.jeontongju.consumer.dto.EmailInfoForAuthRequestDto;
import com.jeontongju.consumer.dto.ErrorFormat;
import com.jeontongju.consumer.dto.SuccessFormat;
import com.jeontongju.consumer.service.ConsumerService;
import com.jeontongju.consumer.utils.CustomErrMessage;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsumerRestController {

  private final ConsumerService consumerService;

  @PostMapping("/sign-up/email/auth")
  public ResponseEntity<SuccessFormat> sendEmailAuthForSignUp(
      @Valid @RequestBody EmailInfoForAuthRequestDto emailInfoDto)
      throws MessagingException, UnsupportedEncodingException {
    consumerService.sendEmailAuthForSignUp(emailInfoDto);
    return ResponseEntity.ok()
        .body(
            SuccessFormat.builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("사용자 이메일 유효코드 발송 성공")
                .build());
  }

  @GetMapping("/sign-up/email/auth")
  public ResponseEntity<?> verifyInputCode(@Valid @RequestBody CodeInfoForAuthRequestDto codeInfoDto) {
    if (consumerService.verifyInputCode(codeInfoDto)) {
      return ResponseEntity.ok()
          .body(
              SuccessFormat.builder()
                  .code(HttpStatus.OK.value())
                  .message(HttpStatus.OK.name())
                  .detail("이메일 인증 성공")
                  .build());
    } else {
      return ResponseEntity.badRequest()
          .body(
              ErrorFormat.builder()
                  .code(HttpStatus.BAD_REQUEST.value())
                  .message(HttpStatus.BAD_REQUEST.name())
                  .detail(CustomErrMessage.NOT_VALID_AUTH_CODE)
                  .failure("NOT_VALID_AUTH_CODE")
                  .build());
    }
  }

  @PostMapping("/sign-up")
  public ResponseEntity<SuccessFormat> signUp(
      @Valid @RequestBody CreateConsumerRequestDto createConsumerDto) {
    consumerService.signUp(createConsumerDto);
    return ResponseEntity.ok()
        .body(
            SuccessFormat.builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.name())
                .detail("사용자 일반 회원 가입 성공")
                .build());
  }
}
