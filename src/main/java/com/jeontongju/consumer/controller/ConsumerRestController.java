package com.jeontongju.consumer.controller;

import com.jeontongju.consumer.dto.CreateConsumerRequestDto;
import com.jeontongju.consumer.dto.SuccessFormat;
import com.jeontongju.consumer.service.ConsumerService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsumerRestController {

    private final ConsumerService consumerService;

    @PostMapping("/sign-up")
    public ResponseEntity<SuccessFormat> signUp(@Valid @RequestBody
    CreateConsumerRequestDto createConsumerDto) {
        consumerService.signUp(createConsumerDto);
        return ResponseEntity.ok()
            .body(
                SuccessFormat.builder()
                    .code(200)
                    .message(HttpStatus.OK.name())
                    .detail("사용자 일반 회원 가입 성공")
                    .build());
    }

}
