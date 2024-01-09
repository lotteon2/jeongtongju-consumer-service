package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.domain.common.BaseEntity;
import javax.persistence.*;
import com.jeontongju.consumer.dto.temp.TradePathEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PointHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "trade_id")
  private Long tradeId;

  @Column(name = "trade_point", nullable = false)
  private Long tradePoint;

  @Enumerated(EnumType.STRING)
  @Column(name = "trade_path", nullable = false)
  private TradePathEnum tradePathEnum;

  @Column(name = "point_acc_by_subscription", nullable = false)
  private Long pointAccBySubscription;

  @ManyToOne
  @JoinColumn(name = "consumer_id")
  private Consumer consumer;
}
