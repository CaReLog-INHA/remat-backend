package com.remat.domain.esg.entity;

import com.remat.domain.trade.entity.Trade;
import com.remat.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "esg_report_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ESGReportDetail extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private ESGReport esgReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id", nullable = false)
    private Trade trade;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "carbon_kg", nullable = false)
    private Integer carbonKg;
}
