package com.remat.domain.member.controller.docs;

import com.remat.domain.member.dto.MyPageResDTO;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import com.remat.global.swagger.ApiAuthErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * 마이페이지 API의 Swagger 문서 전용 인터페이스.
 */
@Tag(name = "마이페이지", description = "마이페이지 관련 API")
public interface MyPageApi {

    @Operation(
            summary = "마이페이지 통합 조회",
            description = "로그인 사용자의 프로필, ESG 기여도, 뱃지, 판매 중인 자재, 최근 활동을 한 번에 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.OverviewDTO> getMyPage(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내 프로필 조회",
            description = "로그인 사용자의 기본 정보를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.ProfileDTO> getProfile(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내 ESG 기여도 조회",
            description = "로그인 사용자의 최신 ESG 기여 지표를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.ESGContributionDTO> getESGContribution(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내 ESG 감축 리포트 조회",
            description = "로그인 사용자의 최신 ESG 감축 리포트를 조회합니다. 리포트 기간, 생성일, 요약 지표, 월별 감축량, 거래별 감축 상세를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.ESGReportDTO> getESGReport(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내 뱃지 조회",
            description = "로그인 사용자의 획득한 뱃지와 진행 중인 뱃지를 조회합니다. 진행 중 뱃지는 현재 활동량 기준 진행률을 함께 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.BadgeSectionDTO> getBadges(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내가 판매 중인 자재 조회",
            description = "로그인 사용자가 등록한 판매 중 자재 목록을 조회합니다. 수정 버튼에서 사용할 자재 ID를 포함합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<List<MyPageResDTO.SellingMaterialDTO>> getSellingMaterials(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );

    @Operation(
            summary = "내 최근 활동 조회",
            description = "로그인 사용자의 완료한 구매, 완료한 판매, 받은 리뷰, 작성한 리뷰, 뱃지 획득 내역을 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @ApiAuthErrorResponse
    ApiResponse<MyPageResDTO.RecentActivitiesDTO> getRecentActivities(
            @Parameter(hidden = true) UserDetailsImpl userDetails
    );
}
