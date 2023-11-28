package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.enums.TradePathEnum;
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
@Table(name = "point_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PointHistory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "trade_point", nullable = false)
    private Long tradePoint;

    @Column(name = "trade_path", nullable = false)
    private TradePathEnum tradePathEnum;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;
}
