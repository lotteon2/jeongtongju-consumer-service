package com.jeontongju.consumer.feign.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationClientService {

  private final AuthenticationServiceClient authenticationServiceClient;

  public Boolean isExistSocialAccount(Long memberId) {
    return authenticationServiceClient.isExistSocialAccount(memberId).getData();
  }
}
