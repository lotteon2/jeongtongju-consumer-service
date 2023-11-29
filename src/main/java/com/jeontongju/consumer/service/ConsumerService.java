package com.jeontongju.consumer.service;

import com.jeontongju.consumer.domain.Consumer;
import com.jeontongju.consumer.dto.CodeInfoForAuthRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForSignupRequestDto;
import com.jeontongju.consumer.dto.EmailInfoForAuthRequestDto;
import com.jeontongju.consumer.exception.DuplicateEmailException;
import com.jeontongju.consumer.repository.ConsumerRepository;
import com.jeontongju.consumer.utils.CustomErrMessage;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

  private final ConsumerRepository consumerRepository;
  private final MailService mailService;

  public Consumer signUp(ConsumerInfoForSignupRequestDto consumerInfoDto) {
    return consumerRepository.save(Consumer.create(consumerInfoDto));
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

  public Boolean verifyInputCode(CodeInfoForAuthRequestDto codeInfoDto) {
    return mailService.compareToIssuedCode(codeInfoDto.getValidCode());
  }
}
