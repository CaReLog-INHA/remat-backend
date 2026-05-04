package com.remat.domain.material.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class MaterialReqDTO {

    public record CreateDTO(
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            Integer unit,
            @Schema(description = "자재 상태", allowableValues = {"BEST", "GOOD", "NORMAL"}, example = "GOOD")
            String materialCondition,
            @Schema(description = "거래 유형", allowableValues = {"SALE", "RENTAL"}, example = "SALE")
            String transactionType,
            String imageKey,
            String categoryName,
            String regionName
    ) {}
}
