package com.remat.domain.trade.entity;


import com.remat.domain.member.entity.Member;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "trade")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_request_id", nullable = false)
    private TradeRequest tradeRequest;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "final_price", nullable = false)
    private Integer finalPrice;

    @Column(name = "rental_start")
    private LocalDate rentalStart;

    @Column(name = "rental_end")
    private LocalDate rentalEnd;
}
