package com.jeontongju.consumer.domain;

import com.jeontongju.consumer.domain.common.BaseEntity;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateByKakaoRequestDto;
import com.jeontongju.consumer.dto.ConsumerInfoForCreateRequestDto;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consumer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Consumer extends BaseEntity {

  @Id
  @Column(name = "consumer_id", nullable = false)
  private Long consumerId;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "point", nullable = false)
  @Builder.Default
  private Long point = 0L;

  @Column(name = "auction_credit", nullable = false)
  @Builder.Default
  private Long auctionCredit = 0L;

  @Column(name = "profile_image_url")
  private String profileImageUrl;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "is_first_login", nullable = false)
  @Builder.Default
  private Boolean isFirstLogin = true;

  @Column(name = "is_default", nullable = false)
  @Builder.Default
  private Boolean isAdult = false;

  @Column(name = "is_regular_payment", nullable = false)
  @Builder.Default
  private Boolean isRegularPayment = false;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  @OneToMany(mappedBy = "consumer")
  private List<Address> addressList;

  @OneToMany(mappedBy = "consumer")
  private List<PointHistory> pointHistoryList;

  public static Consumer create(ConsumerInfoForCreateRequestDto createRequestDto) {
    return Consumer.builder()
        .consumerId(createRequestDto.getConsumerId())
        .email(createRequestDto.getEmail())
        .name(createRequestDto.getName())
        .phoneNumber(createRequestDto.getPhoneNumber())
        .build();
  }

  public static Consumer createByKakao(
      ConsumerInfoForCreateByKakaoRequestDto createByKakaoRequestDto) {
    return Consumer.builder()
        .consumerId(createByKakaoRequestDto.getConsumerId())
        .email(createByKakaoRequestDto.getEmail())
        .profileImageUrl(createByKakaoRequestDto.getProfileImageUrl())
        .build();
  }
}
