package com.remat.domain.material.dto;

import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;

import java.time.LocalDateTime;

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
            Integer starRating,
            LocalDateTime createdAt
    ) {}

    public record ImageUploadDTO(
            String imageKey
    ) {}
}
