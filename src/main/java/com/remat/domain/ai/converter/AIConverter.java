package com.remat.domain.ai.converter;

import com.remat.domain.ai.dto.AIAnalysisResDTO;
import com.remat.domain.ai.dto.MatchingResult;
import com.remat.domain.ai.dto.RequiredMaterialDto;
import com.remat.domain.material.entity.Material;

import java.util.ArrayList;
import java.util.List;

public class AIConverter {

    public static AIAnalysisResDTO.AnalysisResultDTO toAnalysisResultDTO(
            Long analysisId,
            MatchingResult matchingResult,
            double totalCarbonReductionKg,
            List<String> resolvedImageUrls
    ) {
        List<AIAnalysisResDTO.MatchedMaterialDTO> matchedDtos =
                toMatchedMaterialDTOs(matchingResult.matchedMaterials(), resolvedImageUrls);

        List<AIAnalysisResDTO.UnmatchedMaterialDTO> unmatchedDtos =
                toUnmatchedMaterialDTOs(matchingResult.unmatchedMaterials());

        return new AIAnalysisResDTO.AnalysisResultDTO(
                analysisId,
                matchedDtos.size(),
                totalCarbonReductionKg,
                matchedDtos,
                unmatchedDtos
        );
    }

    private static List<AIAnalysisResDTO.MatchedMaterialDTO> toMatchedMaterialDTOs(
            List<Material> materials,
            List<String> resolvedImageUrls
    ) {
        List<AIAnalysisResDTO.MatchedMaterialDTO> result = new ArrayList<>();
        for (int i = 0; i < materials.size(); i++) {
            Material m = materials.get(i);
            result.add(new AIAnalysisResDTO.MatchedMaterialDTO(
                    m.getId(),
                    m.getMaterialName(),
                    m.getDescription(),
                    m.getPrice(),
                    m.getQuantity(),
                    resolvedImageUrls.get(i),
                    m.getCategory().getDisplayName(),
                    m.getCategory().getAvgWeightKg() * m.getCategory().getEsgEffect() / 1000.0,
                    m.getTransactionType(),
                    m.getMember().getCompanyName()
            ));
        }
        return result;
    }

    private static List<AIAnalysisResDTO.UnmatchedMaterialDTO> toUnmatchedMaterialDTOs(
            List<RequiredMaterialDto> unmatchedMaterials
    ) {
        return unmatchedMaterials.stream()
                .map(r -> new AIAnalysisResDTO.UnmatchedMaterialDTO(r.name(), r.description()))
                .toList();
    }
}
