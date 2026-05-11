package com.remat.domain.material.controller;

import com.remat.domain.material.dto.MaterialReqDTO;
import com.remat.domain.material.dto.MaterialResDTO;
import java.util.List;
import com.remat.domain.material.entity.enums.MaterialCondition;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.service.MaterialService;
import com.remat.domain.member.entity.Region;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "자재", description = "자재 관련 API")
@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @Operation(
            summary = "자재 목록 조회",
            description = "마켓플레이스의 자재 목록을 최근 등록순으로 조회합니다. 인증 없이 접근 가능합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping
    public ApiResponse<List<MaterialResDTO.ListDTO>> getMaterials(
            @Parameter(description = "카테고리 (미입력 시 전체 조회)", example = "콘크리트")
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "자재 상태 (BEST / GOOD / NORMAL)", example = "BEST")
            @RequestParam(required = false) MaterialCondition materialCondition,
            @Parameter(description = "거래 유형 (SALE / RENTAL)", example = "SALE")
            @RequestParam(required = false) TransactionType transactionType,
            @Parameter(description = "지역 (INCHEON, SEOUL 등)", example = "INCHEON")
            @RequestParam(required = false) Region region
    ) {
        List<MaterialResDTO.ListDTO> resDto = materialService.getMaterials(categoryName, materialCondition, transactionType, region);
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "자재 이미지 업로드",
            description = "자재 이미지를 R2 스토리지에 업로드하고 이미지 key를 반환합니다. 자재 등록 시 반환된 key를 사용하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "업로드 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MaterialResDTO.ImageUploadDTO> uploadImage(
            @Parameter(description = "업로드할 이미지 파일", required = true)
            @RequestPart MultipartFile image
    ) {
        MaterialResDTO.ImageUploadDTO resDto = materialService.uploadImage(image);
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "자재 등록",
            description = "새로운 자재를 등록합니다. 이미지 업로드 후 반환된 key를 imageKey 필드에 입력하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (카테고리 없음 / 잘못된 지역명 / 잘못된 자재 상태 / 잘못된 거래 유형)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @PostMapping
    public ApiResponse<Void> createMaterial(
            @RequestBody @Valid MaterialReqDTO.CreateDTO reqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        materialService.createMaterial(reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }

    @Operation(
            summary = "자재 상세 조회",
            description = "자재 ID로 자재 상세 정보를 조회합니다. 인증 없이 접근 가능합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 자재", content = @Content),
    })
    @GetMapping("/{materialId}")
    public ApiResponse<MaterialResDTO.DetailDTO> getMaterialDetail(
            @Parameter(description = "자재 ID", required = true, example = "1")
            @PathVariable Long materialId
    ) {
        MaterialResDTO.DetailDTO resDto = materialService.getMaterialDetail(materialId);
        return ApiResponse.ok(resDto);
    }
}
