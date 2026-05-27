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
            summary = "거래 요청 승인",
            description = "로그인한 자재 소유자가 받은 판매/대여 거래 요청을 승인하고 완료 거래를 생성합니다. finalPrice 미입력 시 자재 가격으로 저장됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "승인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (대기 상태 아님 / 재고 부족)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "자재 소유자 아님", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 거래 요청", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 생성된 거래", content = @Content),
    })
    @PatchMapping("/requests/{tradeRequestId}/approve")
    public ApiResponse<Void> approveTradeRequest(
            @Parameter(description = "거래 요청 ID", required = true, example = "1")
            @PathVariable Long tradeRequestId,
            @RequestBody(required = false) @Valid TradeReqDTO.ApproveDTO reqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.approveTradeRequest(tradeRequestId, reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }

    @Operation(
            summary = "거래 요청 거절",
            description = "로그인한 자재 소유자가 받은 거래 요청을 거절합니다. 완료 거래는 생성하지 않고 거래 요청 상태만 REJECTED로 변경합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거절 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (대기 상태 아님)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "자재 소유자 아님", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 거래 요청", content = @Content),
    })
    @PatchMapping("/requests/{tradeRequestId}/reject")
    public ApiResponse<Void> rejectTradeRequest(
            @Parameter(description = "거래 요청 ID", required = true, example = "1")
            @PathVariable Long tradeRequestId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.rejectTradeRequest(tradeRequestId, userDetails.getMember());
        return ApiResponse.ok();
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

    @Operation(
            summary = "거래 후기 등록",
            description = "완료된 거래에 대해 거래 참여자가 상대방에게 평점과 후기를 남깁니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 (평점 범위 / 후기 내용 누락)", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "거래 참여자 아님", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 거래", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 리뷰 작성", content = @Content),
    })
    @PostMapping("/{tradeId}/reviews")
    public ApiResponse<Void> createTradeReview(
            @Parameter(description = "거래 ID", required = true, example = "1")
            @PathVariable Long tradeId,
            @RequestBody @Valid TradeReqDTO.ReviewCreateDTO reqDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.createTradeReview(tradeId, reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }
}
