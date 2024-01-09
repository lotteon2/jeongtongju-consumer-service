package com.jeontongju.consumer.feign.authentication;

import io.github.bitbox.bitbox.dto.FeignFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentication-service")
public interface AuthenticationServiceClient {

    @GetMapping("/members/{memberId}/sns-Account/exist")
    FeignFormat<Boolean> isExistSocialAccount(@PathVariable Long memberId);
}
