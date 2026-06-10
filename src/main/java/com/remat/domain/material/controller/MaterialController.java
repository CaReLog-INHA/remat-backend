package com.remat.domain.material.controller;

import com.remat.domain.material.controller.docs.MaterialApi;
import com.remat.domain.material.dto.MaterialReqDTO;
import com.remat.domain.material.dto.MaterialResDTO;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.service.MaterialService;
import com.remat.domain.member.entity.Region;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController implements MaterialApi {

    private final MaterialService materialService;

    @Override
    @GetMapping
    public ApiResponse<List<MaterialResDTO.ListDTO>> getMaterials(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) MaterialCondition materialCondition,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) Region region
    ) {
        List<MaterialResDTO.ListDTO> resDto = materialService.getMaterials(categoryName, materialCondition, transactionType, region);
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<List<MaterialResDTO.MyListDTO>> getMyMaterials(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MaterialResDTO.MyListDTO> resDto = materialService.getMyMaterials(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MaterialResDTO.ImageUploadDTO> uploadImage(
            @RequestPart MultipartFile image
    ) {
        MaterialResDTO.ImageUploadDTO resDto = materialService.uploadImage(image);
        return ApiResponse.ok(resDto);
    }

    @Override
    @PostMapping
    public ApiResponse<Void> createMaterial(
            @RequestBody @Valid MaterialReqDTO.CreateDTO reqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        materialService.createMaterial(reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }

    @Override
    @GetMapping("/{materialId}")
    public ApiResponse<MaterialResDTO.DetailDTO> getMaterialDetail(
            @PathVariable Long materialId
    ) {
        MaterialResDTO.DetailDTO resDto = materialService.getMaterialDetail(materialId);
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/categories")
    public ApiResponse<MaterialResDTO.CategoryListDTO> getCategories() {
        MaterialResDTO.CategoryListDTO resDto = materialService.getCategories();
        return ApiResponse.ok(resDto);
    }
}
