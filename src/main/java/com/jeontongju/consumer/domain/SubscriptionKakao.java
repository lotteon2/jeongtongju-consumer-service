package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription_kakao")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SubscriptionKakao {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "kakao_subscription_id")
    private Long kakaoSubscriptionId;

    @Column(name = "subscription_unique_key", nullable = false)
    private String subscriptionUniqueKey;

    @OneToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;
}
