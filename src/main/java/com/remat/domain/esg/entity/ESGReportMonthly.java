package com.remat.domain.esg.entity;

import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "esg_report_monthly")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ESGReportMonthly extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ESGReport esgReport;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(name = "carbon_kg", nullable = false)
    private Integer carbonKg;
}
