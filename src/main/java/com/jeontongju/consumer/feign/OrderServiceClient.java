package com.jeontongju.consumer.feign;

import com.jeontongju.consumer.dto.temp.FeignFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

  @GetMapping("/sellers/{sellerId}/orders-consumer/ids")
  FeignFormat<List<Long>> getConsumerOrderIdsBySellerId(@PathVariable("sellerId") Long sellerId);
}
