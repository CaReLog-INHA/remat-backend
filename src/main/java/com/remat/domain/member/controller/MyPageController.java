package com.remat.domain.member.controller;

import com.remat.domain.member.dto.MyPageResDTO;
import com.remat.domain.member.service.MyPageService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "마이페이지", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(
            summary = "마이페이지 통합 조회",
            description = "로그인 사용자의 프로필, ESG 기여도, 뱃지, 판매 중인 자재, 최근 활동을 한 번에 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping
    public ApiResponse<MyPageResDTO.OverviewDTO> getMyPage(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.OverviewDTO resDto = myPageService.getMyPage(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내 프로필 조회",
            description = "로그인 사용자의 기본 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/profile")
    public ApiResponse<MyPageResDTO.ProfileDTO> getProfile(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ProfileDTO resDto = myPageService.getProfile(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내 ESG 기여도 조회",
            description = "로그인 사용자의 최신 ESG 기여 지표를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/esg")
    public ApiResponse<MyPageResDTO.ESGContributionDTO> getESGContribution(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ESGContributionDTO resDto = myPageService.getESGContribution(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내 ESG 감축 리포트 조회",
            description = "로그인 사용자의 최신 ESG 감축 리포트를 조회합니다. 리포트 기간, 생성일, 요약 지표, 월별 감축량, 거래별 감축 상세를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/esg-report")
    public ApiResponse<MyPageResDTO.ESGReportDTO> getESGReport(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.ESGReportDTO resDto = myPageService.getESGReport(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내 뱃지 조회",
            description = "로그인 사용자의 획득한 뱃지와 진행 중인 뱃지를 조회합니다. 진행 중 뱃지는 현재 활동량 기준 진행률을 함께 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/badges")
    public ApiResponse<MyPageResDTO.BadgeSectionDTO> getBadges(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.BadgeSectionDTO resDto = myPageService.getBadges(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내가 판매 중인 자재 조회",
            description = "로그인 사용자가 등록한 판매 중 자재 목록을 조회합니다. 수정 버튼에서 사용할 자재 ID를 포함합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/materials")
    public ApiResponse<List<MyPageResDTO.SellingMaterialDTO>> getSellingMaterials(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<MyPageResDTO.SellingMaterialDTO> resDto = myPageService.getSellingMaterials(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내 최근 활동 조회",
            description = "로그인 사용자의 완료한 구매, 완료한 판매, 받은 리뷰, 작성한 리뷰, 뱃지 획득 내역을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/activities")
    public ApiResponse<MyPageResDTO.RecentActivitiesDTO> getRecentActivities(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        MyPageResDTO.RecentActivitiesDTO resDto = myPageService.getRecentActivities(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }
}
