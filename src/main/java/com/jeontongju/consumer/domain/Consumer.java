package com.jeontongju.consumer.domain;

import static javax.persistence.GenerationType.IDENTITY;

import com.jeontongju.consumer.domain.common.BaseEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
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

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.REMOVE)
    private List<SnsAccount> snsAccountList;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.REMOVE)
    private List<Address> addressList;
}
