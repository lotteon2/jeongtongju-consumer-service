package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.CreateConsumerRequestDto;
import com.jeontongju.consumer.dto.EmailInfoForAuthRequestDto;
import com.jeontongju.consumer.exception.DuplicateEmailException;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

  private final ConsumerRepository consumerRepository;
  private final MailService mailService;

  public Consumer signUp(CreateConsumerRequestDto createConsumerDto) {
    return consumerRepository.save(Consumer.create(createConsumerDto));
  }

  public void sendEmailAuthForSignUp(EmailInfoForAuthRequestDto emailInfoDto)
      throws MessagingException, UnsupportedEncodingException {
    if (isNotEmailDuplicated(emailInfoDto.getEmail())) {
      log.info("email: " + emailInfoDto.getEmail());
      mailService.sendAuthEmail(emailInfoDto.getEmail());
      return;
    }
    throw new DuplicateEmailException(CustomErrMessage.EMAIL_ALREADY_IN_USE);
  }

  private Boolean isNotEmailDuplicated(String email) {
    Consumer foundConsumer = consumerRepository.findByEmail(email).orElse(null);
    return foundConsumer == null;
  }
}
