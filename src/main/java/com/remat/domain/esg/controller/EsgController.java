package com.remat.domain.esg.controller;

import com.remat.domain.esg.controller.docs.EsgApi;
import com.remat.domain.esg.dto.EsgReqDTO;
import com.remat.domain.esg.dto.EsgResDTO;
import com.remat.domain.esg.service.EsgService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/esg-reports")
@RequiredArgsConstructor
public class EsgController implements EsgApi {

    private final EsgService esgService;

    @Override
    @PostMapping
    public ApiResponse<EsgResDTO.EsgReportDTO> getEsgReports(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody EsgReqDTO.EsgReportDateDTO reqDto
    ) {
        EsgResDTO.EsgReportDTO resDto = esgService.getEsgReports(userDetails.getMember(), reqDto);
        return ApiResponse.ok(resDto);
    }
}
