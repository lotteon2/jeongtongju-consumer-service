package com.jeontongju.consumer.service;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MailServiceTests {

    @Value("${custom.my.email.to}")
    private String toEmail;

    @Autowired
    private MailService mailService;

    @Test
    @DisplayName("사용자는 회원가입 시, 이메일 인증을 위한 이메일을 발송할 수 있다.")
    void test1() throws MessagingException, UnsupportedEncodingException {
        mailService.sendAuthEmail(toEmail);
    }
}
