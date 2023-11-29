package com.jeontongju.consumer.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.ConsumerInfoForSignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ConsumerServiceTests {

  @Autowired private ConsumerService consumerService;

  @Test
  @DisplayName("사용자는 회원가입을 할 수 있다.")
  void test1() {
    ConsumerInfoForSignupRequestDto dto =
        ConsumerInfoForSignupRequestDto.builder()
            .email("test1@naver.com")
            .password("1234")
            .name("이름1")
            .build();
    Consumer savedConsumer = consumerService.signUp(dto);
    assertThat(savedConsumer.getConsumerId()).isEqualTo(1L);
  }
}
