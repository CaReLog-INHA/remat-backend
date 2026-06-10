package com.remat.domain.member.controller;

import com.remat.domain.member.controller.docs.MyPageApi;
import com.remat.domain.member.dto.MyPageResDTO;
import com.remat.domain.member.service.MyPageService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController implements MyPageApi {

    private final MyPageService myPageService;

    @Override
    @GetMapping
    public ApiResponse<MyPageResDTO.OverviewDTO> getMyPage(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.OverviewDTO resDto = myPageService.getMyPage(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/profile")
    public ApiResponse<MyPageResDTO.ProfileDTO> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ProfileDTO resDto = myPageService.getProfile(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/esg")
    public ApiResponse<MyPageResDTO.ESGContributionDTO> getESGContribution(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ESGContributionDTO resDto = myPageService.getESGContribution(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/esg-report")
    public ApiResponse<MyPageResDTO.ESGReportDTO> getESGReport(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ESGReportDTO resDto = myPageService.getESGReport(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/badges")
    public ApiResponse<MyPageResDTO.BadgeSectionDTO> getBadges(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.BadgeSectionDTO resDto = myPageService.getBadges(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/materials")
    public ApiResponse<List<MyPageResDTO.SellingMaterialDTO>> getSellingMaterials(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MyPageResDTO.SellingMaterialDTO> resDto = myPageService.getSellingMaterials(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/activities")
    public ApiResponse<MyPageResDTO.RecentActivitiesDTO> getRecentActivities(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.RecentActivitiesDTO resDto = myPageService.getRecentActivities(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }
}
