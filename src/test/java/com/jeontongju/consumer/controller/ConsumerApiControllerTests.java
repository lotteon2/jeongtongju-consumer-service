package com.jeontongju.consumer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeontongju.consumer.dto.ConsumerInfoForSignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles(value = {"test"})
public class ConsumerApiControllerTests {

  @Autowired private MockMvc mvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("회원가입 valid Test, 정상 동작")
  void t1() throws Exception {

    ConsumerInfoForSignupRequestDto dto =
        ConsumerInfoForSignupRequestDto.builder()
            .email("jtjj@naver.com")
            .password("jtjj1234@")
            .name("전통주")
            .build();

    String body = objectMapper.writeValueAsString(dto);

    ResultActions resultActions =
        mvc.perform(post("/api/sign-up").contentType(MediaType.APPLICATION_JSON).content(body));

    resultActions.andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("회원가입 valid Test, 비밀번호 형식 오류")
  void t2() throws Exception {

    ConsumerInfoForSignupRequestDto dto =
        ConsumerInfoForSignupRequestDto.builder()
            .email("jtjj@naver.com")
            .password("jtjj1234")
            .name("전통주")
            .build();

    String body = objectMapper.writeValueAsString(dto);

    ResultActions resultActions =
        mvc.perform(post("/api/sign-up").contentType(MediaType.APPLICATION_JSON).content(body));

    resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }
}
