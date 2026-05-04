package com.remat.domain.material.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaterialReqDTO {

    public record CreateDTO(
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            Integer unit,
            @Schema(description = "자재 상태", allowableValues = {"BEST", "GOOD", "NORMAL"}, example = "GOOD")
            @NotBlank
            String materialCondition,
            @Schema(description = "거래 유형", allowableValues = {"SALE", "RENTAL"}, example = "SALE")
            @NotBlank
            String transactionType,
            String imageKey,
            String categoryName,
            String regionName
    ) {}
}
