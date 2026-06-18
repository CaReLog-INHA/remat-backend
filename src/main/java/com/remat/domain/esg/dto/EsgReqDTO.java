package com.remat.domain.esg.dto;

import java.time.LocalDate;

public class EsgReqDTO {

    public record EsgReportDateDTO(
            LocalDate periodStart,
            LocalDate periodEnd
    ) {}
}
