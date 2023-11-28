package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.domain.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Address extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "address_id")
  private Long addressId;

  @Column(name = "basic_address", nullable = false)
  private String basicAddress;

  @Column(name = "address_detail")
  private String addressDetail;

  @Column(name = "zone_code", nullable = false)
  private String zoneCode;

  @Column(name = "recipient_name", nullable = false)
  private String recipientName;

  @Column(name = "recipient_phone_number", nullable = false)
  private String recipientPhoneNumber;

  @Column(name = "is_default", nullable = false)
  @Builder.Default
  private Boolean isDefault = false;

  @ManyToOne
  @JoinColumn(name = "consumer_id")
  private Consumer consumer;
}
