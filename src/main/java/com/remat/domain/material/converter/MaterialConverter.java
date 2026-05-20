package com.remat.domain.material.converter;

import com.remat.domain.material.dto.MaterialReqDTO;
import com.remat.domain.material.dto.MaterialResDTO;
import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.MaterialCategory;
import com.remat.domain.member.entity.Member;

public class MaterialConverter {

    public static Material toEntity(
            MaterialReqDTO.CreateDTO reqDto,
            Member member,
            MaterialCategory category
    ) {
        return Material.builder()
                .member(member)
                .category(category)
                .region(reqDto.region())
                .materialName(reqDto.materialName())
                .description(reqDto.description())
                .price(reqDto.price())
                .quantity(reqDto.quantity())
                .unit(reqDto.unit())
                .materialCondition(reqDto.materialCondition())
                .transactionType(reqDto.transactionType())
                .imageKey(reqDto.imageKey())
                .build();
    }

    public static MaterialResDTO.ListDTO toListDTO(Material material, String imageUrl) {
        return new MaterialResDTO.ListDTO(
                material.getId(),
                material.getMaterialName(),
                material.getPrice(),
                material.getQuantity(),
                material.getUnit(),
                material.getMaterialCondition(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getRegion().getKoreanName(),
                material.getCreatedAt()
        );
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
                material.getMember().getStarRating(),
                material.getCreatedAt()
        );
    }
}
