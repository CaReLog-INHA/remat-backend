package com.remat.domain.trade.entity;

import com.remat.domain.material.entity.Material;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.enums.RequestStatus;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "trade_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TradeRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_member_id", nullable = false)
    private Member requestMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_material_id", nullable = false)
    private Material requestMaterial;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "request_message", nullable = false)
    private String requestMessage;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Column(name = "rental_start")
    private LocalDate rentalStart;

    @Column(name = "rental_end")
    private LocalDate rentalEnd;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
