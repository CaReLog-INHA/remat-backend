package com.remat.domain.esg.converter;

import com.remat.domain.esg.dto.EsgResDTO;
import com.remat.domain.esg.entity.ESGReport;
import com.remat.domain.esg.entity.ESGReportDetail;
import com.remat.domain.esg.entity.ESGReportMonthly;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.Trade;

import java.time.LocalDate;
import java.util.List;

public class EsgConverter {

    public static ESGReportDetail toEsgReportDetail(Trade trade, Double carbonReductionKg) {
        return ESGReportDetail.builder()
                .trade(trade)
                .tradeDate(trade.getCreatedAt().toLocalDate())
                .materialName(trade.getTradeRequest().getRequestMaterial().getMaterialName())
                .quantity(trade.getTradeRequest().getQuantity())
                .carbonKg(carbonReductionKg)
                .build();
    }

    public static ESGReport toEsgReport(
            Member member,
            Double totalCarbonReductionKg,
            Integer resourceReuseRate,
            Integer treeCount,
            Integer tradeCount,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {
        return ESGReport.builder()
                .member(member)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .totalCarbonKg(totalCarbonReductionKg)
                .totalTradeCount(tradeCount)
                .resourceReuseRate(resourceReuseRate)
                .treeCount(treeCount)
                .build();
    }

    public static EsgResDTO.EsgReportDTO toEsgReportDTO(ESGReport esgReport) {
        List<EsgResDTO.EsgReportDTO.EsgReportDetailDTO> detailDTOs = esgReport.getReportDetails().stream()
                .map(EsgConverter::toEsgReportDetailDTO)
                .toList();

        List<EsgResDTO.EsgReportDTO.EsgReportMonthlyDTO> monthlyDTOs = esgReport.getMonthlyReports().stream()
                .map(EsgConverter::toEsgReportMonthlyDTO)
                .toList();

        return new EsgResDTO.EsgReportDTO(
                esgReport.getId(),
                esgReport.getPeriodStart(),
                esgReport.getPeriodEnd(),
                esgReport.getTotalCarbonKg(),
                esgReport.getTotalTradeCount(),
                esgReport.getResourceReuseRate(),
                esgReport.getTreeCount(),
                detailDTOs,
                monthlyDTOs
        );
    }

    private static EsgResDTO.EsgReportDTO.EsgReportDetailDTO toEsgReportDetailDTO(ESGReportDetail detail) {
        return new EsgResDTO.EsgReportDTO.EsgReportDetailDTO(
                detail.getTrade().getId(),
                detail.getTradeDate(),
                detail.getMaterialName(),
                detail.getQuantity(),
                detail.getCarbonKg()
        );
    }

    private static EsgResDTO.EsgReportDTO.EsgReportMonthlyDTO toEsgReportMonthlyDTO(ESGReportMonthly monthly) {
        return new EsgResDTO.EsgReportDTO.EsgReportMonthlyDTO(
                monthly.getYear(),
                monthly.getMonth(),
                monthly.getCarbonKg()
        );
    }
}
