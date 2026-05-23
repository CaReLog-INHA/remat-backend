package com.remat.domain.ai.controller;

import com.remat.domain.ai.dto.AIAnalysisResDTO;
import com.remat.domain.ai.service.AIAnalysisService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "AI 자재 분석", description = "기획서 PDF를 업로드하면 AI가 필요한 자재를 분석하고 추천합니다")
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIAnalysisController {

    private final AIAnalysisService aiAnalysisService;

    @Operation(
            summary = "AI 자재 분석",
            description = "기획서 PDF를 업로드하면 AI가 필요한 자재를 추출하고 등록된 자재와 매칭하여 결과를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "분석 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 파일 (PDF 아님 / 파일 없음 / 50MB 초과 / 텍스트 추출 불가)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "AI 분석 실패", content = @Content),
    })
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AIAnalysisResDTO.AnalysisResultDTO> analyze(
            @Parameter(description = "분석할 기획서 PDF (최대 50MB)", required = true)
            @RequestPart MultipartFile file,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AIAnalysisResDTO.AnalysisResultDTO result = aiAnalysisService.analyze(file, userDetails.getMember());
        return ApiResponse.ok(result);
    }
}
