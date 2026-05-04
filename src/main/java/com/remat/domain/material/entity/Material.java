package com.remat.domain.material.entity;

import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.Region;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "material")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Material extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MaterialCategory category;

    @Column(name = "region", nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(name = "material_name", length = 50, nullable = false)
    private String materialName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "material_condition", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialCondition materialCondition;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @Column(name = "unit", nullable = false)
    private Integer unit;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
