package com.remat.domain.trade.controller;

import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
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

import java.util.List;

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

    @Operation(
            summary = "받은 거래 요청 목록 조회",
            description = "로그인한 회원이 등록한 자재에 대해 들어온 거래 요청 목록을 최신순으로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/requests/received")
    public ApiResponse<List<TradeResDTO.ReceivedRequestDTO>> getReceivedTradeRequests(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.ReceivedRequestDTO> resDto = tradeService.getReceivedTradeRequests(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "보낸 거래 요청 목록 조회",
            description = "로그인한 회원이 보낸 거래 요청 목록을 최신순으로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/requests/sent")
    public ApiResponse<List<TradeResDTO.SentRequestDTO>> getSentTradeRequests(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.SentRequestDTO> resDto = tradeService.getSentTradeRequests(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내가 구매한 거래 내역 조회",
            description = "로그인한 회원이 구매자로 참여한 완료 거래 내역을 최신순으로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/history/purchased")
    public ApiResponse<List<TradeResDTO.PurchasedTradeDTO>> getPurchasedTrades(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.PurchasedTradeDTO> resDto = tradeService.getPurchasedTrades(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Operation(
            summary = "내가 판매한 거래 내역 조회",
            description = "로그인한 회원이 판매자로 참여한 완료 거래 내역을 최신순으로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
    })
    @GetMapping("/history/sold")
    public ApiResponse<List<TradeResDTO.SoldTradeDTO>> getSoldTrades(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.SoldTradeDTO> resDto = tradeService.getSoldTrades(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }
}
