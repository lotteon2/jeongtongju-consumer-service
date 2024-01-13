package com.jeontongju.consumer.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
public class PaginationManager<T> {

  public Page<T> wrapByPage(List<T> histories, Pageable pageable, Long totalSize) {

    return new PageImpl<>(histories, pageable, totalSize);
  }

  public Pageable getPageableByCreatedAt(int page, int size) {

    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("createdAt"));
    return PageRequest.of(page, size);
  }
}
