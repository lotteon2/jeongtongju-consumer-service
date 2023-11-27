package com.jeontongju.consumer.domain;

import com.jeontongju.consumer.domain.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sns_account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SnsAccount extends BaseEntity {
    @Id
    @Column(name = "sns_unique_id")
    private String snsUniqueId;

    @Column(name = "oauth_provider", nullable = false)
    private String oauthProvider;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;
}
