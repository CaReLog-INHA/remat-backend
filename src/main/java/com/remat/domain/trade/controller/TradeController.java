package com.remat.domain.trade.controller;

import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.service.TradeService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래", description = "거래 관련 API")
@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @Operation(
            summary = "거래 요청",
            description = "자재에 대한 구매 또는 대여 요청을 생성합니다. 대여(RENTAL) 자재인 경우 rentalStart, rentalEnd 필드가 필수입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (수량 초과 / 본인 자재 / 대여 기간 누락 또는 유효하지 않음)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 자재", content = @Content),
    })
    @PostMapping
    public ApiResponse<Void> createTradeRequest(
            @RequestBody @Valid TradeReqDTO.CreateDTO reqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.createTradeRequest(reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }
}
