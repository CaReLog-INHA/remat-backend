package com.remat.domain.esg.entity;

import com.remat.domain.member.entity.Member;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "esg_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ESGReport extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "total_carbon_kg", nullable = false)
    private Integer totalCarbonKg;

    @Column(name = "total_trade_count", nullable = false)
    private Integer totalTradeCount;

    @Column(name = "resource_reuse_rate", nullable = false)
    private Integer resourceReuseRate;

    @Column(name = "tree_count", nullable = false)
    private Integer treeCount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "esgReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ESGReportMonthly> monthlyReports = new ArrayList<>();

    @OneToMany(mappedBy = "esgReport", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ESGReportDetail> reportDetails = new ArrayList<>();
}
