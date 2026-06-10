package com.remat.domain.ai.controller;

import com.remat.domain.ai.controller.docs.AIAnalysisApi;
import com.remat.domain.ai.dto.AIAnalysisResDTO;
import com.remat.domain.ai.service.AIAnalysisService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIAnalysisController implements AIAnalysisApi {

    private final AIAnalysisService aiAnalysisService;

    @Override
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AIAnalysisResDTO.AnalysisResultDTO> analyze(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        AIAnalysisResDTO.AnalysisResultDTO result = aiAnalysisService.analyze(file, userDetails.getMember());
        return ApiResponse.ok(result);
    }
}
