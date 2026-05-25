package com.remat.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RequiredMaterialDto(
        String name,
        String description,
        Integer quantity,
        String specs
) {}
