package com.remat.domain.material.converter;

import com.remat.domain.material.dto.MaterialReqDTO;
import com.remat.domain.material.dto.MaterialResDTO;
import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.MaterialCategory;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.Region;

public class MaterialConverter {

    public static Material toEntity(
            MaterialReqDTO.CreateDTO reqDto,
            Member member,
            MaterialCategory category,
            Region region,
            MaterialCondition condition,
            TransactionType transactionType
    ) {
        return Material.builder()
                .member(member)
                .category(category)
                .region(region)
                .materialName(reqDto.materialName())
                .description(reqDto.description())
                .price(reqDto.price())
                .quantity(reqDto.quantity())
                .unit(reqDto.unit())
                .materialCondition(condition)
                .transactionType(transactionType)
                .imageKey(reqDto.imageKey())
                .build();
    }

    public static MaterialResDTO.DetailDTO toDetailDTO(Material material, String imageUrl) {
        return new MaterialResDTO.DetailDTO(
                material.getId(),
                material.getMaterialName(),
                material.getDescription(),
                material.getPrice(),
                material.getQuantity(),
                material.getUnit(),
                material.getMaterialCondition(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getCategory().getEsgEffect(),
                material.getRegion().getKoreanName(),
                material.getMember().getName(),
                material.getMember().getCompanyName(),
                material.getCreatedAt()
        );
    }
}
