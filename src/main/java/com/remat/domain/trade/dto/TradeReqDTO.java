package com.remat.domain.trade.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TradeReqDTO {

    public record CreateDTO(
            @NotNull Long materialId,
            @NotNull @Min(1) Integer quantity,
            @NotBlank String requestMessage,
            LocalDate rentalStart,
            LocalDate rentalEnd
    ) {}
}
