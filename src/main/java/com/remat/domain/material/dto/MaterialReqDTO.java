package com.remat.domain.material.dto;

import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.member.entity.Region;
import jakarta.validation.constraints.NotNull;

public class MaterialReqDTO {

    public record CreateDTO(
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            Integer unit,
            @NotNull
            MaterialCondition materialCondition,
            @NotNull
            TransactionType transactionType,
            String imageKey,
            String categoryName,
            @NotNull
            Region region
    ) {}
}
