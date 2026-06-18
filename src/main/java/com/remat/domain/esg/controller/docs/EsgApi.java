package com.remat.domain.esg.controller.docs;

import com.remat.domain.esg.dto.EsgReqDTO;
import com.remat.domain.esg.dto.EsgResDTO;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import com.remat.global.swagger.ApiAuthErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "ESG 리포트", description = "ESG 탄소 감축 리포트 API")
public interface EsgApi {

    @Operation(
            summary = "ESG 리포트 생성",
            description = "선택한 기간(periodStart ~ periodEnd)의 완료 거래를 기반으로 ESG 탄소 감축 리포트를 새로 생성(스냅샷 저장)하고 반환합니다. "
                    + "기간 내 총 탄소 감축량, 거래 건수, 자원 재활용률, 나무 환산 그루 수와 함께 월별 감축 추이·거래별 감축 내역을 제공합니다. "
                    + "마이페이지 ESG 지표는 가장 최근에 생성된 리포트를 사용합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (기간 누락 또는 유효하지 않음)", content = @Content),
    })
    @ApiAuthErrorResponse
    ApiResponse<EsgResDTO.EsgReportDTO> getEsgReports(
            @Parameter(hidden = true) UserDetailsImpl userDetails,
            EsgReqDTO.EsgReportDateDTO reqDto
    );
}
