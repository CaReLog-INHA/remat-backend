package com.remat.domain.esg.dto;

import java.time.LocalDate;
import java.util.List;

public class EsgResDTO {

    public record EsgReportDTO(
            Long id,
            LocalDate periodStart,
            LocalDate periodEnd,
            Double totalCarbonKg,
            Integer totalTradeCount,
            Integer resourceReuseRate,
            Integer treeCount,
            List<EsgReportDetailDTO> detailDTOs,
            List<EsgReportMonthlyDTO> monthlyDTOs
    ) {
        public record EsgReportDetailDTO(
                Long tradeId,
                LocalDate tradeDate,
                String materialName,
                Integer quantity,
                Double carbonKg
        ) {}

        public record EsgReportMonthlyDTO(
                Integer year,
                Integer month,
                Double carbonKg
        ) {}
    }
}
