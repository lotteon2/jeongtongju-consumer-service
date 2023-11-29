package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.domain.common.BaseEntity;
import com.jeontongju.consumer.enums.PaymentTypeEnum;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Subscription extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "subscription_id")
  private Long subscriptionId;

  @Column(name = "subscription_type", nullable = false)
  private String subscriptionType;

  @Column(name = "payment_amount", nullable = false)
  private Long paymentAmount;

  @Column(name = "start_date", nullable = false)
  private Timestamp startDate;

  @Column(name = "end_date", nullable = false)
  private String endDate;

  @Column(name = "payment_type", nullable = false)
  private PaymentTypeEnum paymentType;

  @OneToOne(mappedBy = "subscription")
  private SubscriptionKakao subscriptionKakao;
}
