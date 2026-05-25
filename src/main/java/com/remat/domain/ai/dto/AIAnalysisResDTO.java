package com.remat.domain.ai.dto;

import com.remat.domain.material.entity.enums.TransactionType;

import java.util.List;

public class AIAnalysisResDTO {

    public record AnalysisResultDTO(
            Long analysisId,
            int matchedCount,
            double totalCarbonReductionKg,
            List<MatchedMaterialDTO> matchedMaterials,
            List<UnmatchedMaterialDTO> requiredMaterials
    ) {}

    public record MatchedMaterialDTO(
            Long materialId,
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            String imageUrl,
            String categoryName,
            double carbonReductionKg,
            TransactionType transactionType,
            String sellerName
    ) {}

    public record UnmatchedMaterialDTO(
            String name,
            String description
    ) {}
}
