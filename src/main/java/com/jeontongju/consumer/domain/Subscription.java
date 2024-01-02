package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.domain.common.BaseEntity;
import io.github.bitbox.bitbox.enums.PaymentMethodEnum;
import io.github.bitbox.bitbox.enums.SubscriptionTypeEnum;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

  @ManyToOne
  @JoinColumn(name = "consumer_id", nullable = false)
  private Consumer consumer;

  @Column(name = "subscription_type", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private SubscriptionTypeEnum subscriptionType;

  @Column(name = "payment_amount", nullable = false)
  @NotNull
  private Long paymentAmount;

  @Column(name = "start_date", nullable = false)
  @NotNull
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  @NotNull
  private LocalDateTime endDate;

  @Column(name = "payment_type", nullable = false)
  @Enumerated(EnumType.STRING)
  @NotNull
  private PaymentMethodEnum paymentMethod;
}
