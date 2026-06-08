package com.remat.domain.material.dto;

import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class MaterialResDTO {

    public record ListDTO(
            Long id,
            String materialName,
            Integer price,
            Integer quantity,
            Integer unit,
            MaterialCondition materialCondition,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            LocalDateTime createdAt
    ) {}

    public record MyListDTO(
            Long id,
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            Integer unit,
            MaterialCondition materialCondition,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            LocalDateTime createdAt
    ) {}

    public record DetailDTO(
            Long id,
            String materialName,
            String description,
            Integer price,
            Integer quantity,
            Integer unit,
            MaterialCondition materialCondition,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            Integer esgEffect,
            String region,
            String sellerName,
            String companyName,
            Double carbonReductionKg,
            Integer starRating,
            LocalDateTime createdAt
    ) {}

    public record ImageUploadDTO(
            String imageKey
    ) {}

    public record CategoryListDTO(
            List<CategoryDTO> categories
    ) {
        public record CategoryDTO(
                Long id,
                String categoryName,
                String displayName
        ) {}
    }
}
