package com.remat.domain.ai.dto;

import com.remat.domain.material.entity.Material;

import java.util.List;

public record MatchingResult(
        List<Material> matchedMaterials,
        List<RequiredMaterialDto> unmatchedMaterials
) {}
