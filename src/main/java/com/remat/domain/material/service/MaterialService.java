package com.remat.domain.material.service;

import com.remat.domain.material.converter.MaterialConverter;
import com.remat.domain.material.dto.MaterialReqDTO;
import com.remat.domain.material.dto.MaterialResDTO;
import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.MaterialCategory;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.exception.MaterialException;
import com.remat.domain.material.exception.enums.MaterialErrorCode;
import com.remat.domain.material.repository.MaterialCategoryRepository;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.entity.Member;
import com.remat.domain.member.entity.Region;
import com.remat.global.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialCategoryRepository materialCategoryRepository;
    private final R2Service r2Service;

    @Transactional
    public MaterialResDTO.ImageUploadDTO uploadImage(MultipartFile image) {
        String key = r2Service.upload(image, "materials");
        return new MaterialResDTO.ImageUploadDTO(key);
    }

    @Transactional
    public void createMaterial(MaterialReqDTO.CreateDTO reqDto, Member member) {
        MaterialCategory category = materialCategoryRepository.findByDisplayName(reqDto.categoryName())
                .orElseThrow(() -> new MaterialException(MaterialErrorCode.CATEGORY_NOT_FOUND));

        Region region;
        try {
            region = Region.fromKoreanName(reqDto.regionName());
        } catch (IllegalArgumentException e) {
            throw new MaterialException(MaterialErrorCode.INVALID_REGION_NAME);
        }

        MaterialCondition condition;
        try {
            condition = MaterialCondition.valueOf(reqDto.materialCondition());
        } catch (IllegalArgumentException e) {
            throw new MaterialException(MaterialErrorCode.INVALID_MATERIAL_CONDITION);
        }

        TransactionType transactionType;
        try {
            transactionType = TransactionType.valueOf(reqDto.transactionType());
        } catch (IllegalArgumentException e) {
            throw new MaterialException(MaterialErrorCode.INVALID_TRANSACTION_TYPE);
        }

        Material material = MaterialConverter.toEntity(reqDto, member, category, region, condition, transactionType);
        materialRepository.save(material);
    }

    public MaterialResDTO.DetailDTO getMaterialDetail(Long materialId) {
        Material material = materialRepository.findByIdAndDeletedAtIsNull(materialId)
                .orElseThrow(() -> new MaterialException(MaterialErrorCode.MATERIAL_NOT_FOUND));

        String imageUrl = r2Service.getFileUrl(material.getImageKey());
        return MaterialConverter.toDetailDTO(material, imageUrl);
    }
}
