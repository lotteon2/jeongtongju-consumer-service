package com.jeontongju.consumer.service;

import com.jeontongju.consumer.dto.MailInfoDto;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
  @Value("${custom.my.email.from}")
  private String from;

  private final JavaMailSender mailSender;
  private final Integer VALID_CODE_LENGTH = 8;

  public String createValidCode() {
    Random random = new Random();
    StringBuffer key = new StringBuffer();

    String authNum = "";
    for (int i = 0; i < VALID_CODE_LENGTH; i++) {
      int idx = random.nextInt(3);

      switch (idx) {
        case 0:
          key.append((char) ((int) random.nextInt(26) + 97));
          break;
        case 1:
          key.append((char) ((int) random.nextInt(26) + 65));
          break;
        case 2:
          key.append(random.nextInt(9));
          break;
      }
    }
    authNum = key.toString();
    return authNum;
  }

  public MailInfoDto createEmailForm(String to)
      throws MessagingException, UnsupportedEncodingException {
    String setFrom = from;
    String setTo = to;
    String title = "전통주점 회원가입 유효코드 발송";
    String authNum = createValidCode();
    MimeMessage message = mailSender.createMimeMessage();
    message.addRecipients(RecipientType.TO, setTo);
    message.setSubject(title);
    message.setFrom(setFrom);
    message.setText("회원가입 인증 유효코드입니다.<br>" + authNum, "utf-8", "html");
    return MailInfoDto.builder().mimeMessage(message).validCode(authNum).build();
  }

  public String sendAuthEmail(String email)
      throws MessagingException, UnsupportedEncodingException {
    MailInfoDto mailInfo = createEmailForm(email);
    MimeMessage emailForm = mailInfo.getMimeMessage();
    mailSender.send(emailForm);
    return mailInfo.getValidCode();
  }
}
