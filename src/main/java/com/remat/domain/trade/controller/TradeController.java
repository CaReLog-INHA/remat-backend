package com.remat.domain.trade.controller;

import com.remat.domain.trade.controller.docs.TradeApi;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
import com.remat.domain.trade.service.TradeService;
import com.remat.global.auth.UserDetailsImpl;
import com.remat.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController implements TradeApi {

    private final TradeService tradeService;

    @Override
    @PostMapping
    public ApiResponse<Void> createTradeRequest(
            @RequestBody @Valid TradeReqDTO.CreateDTO reqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.createTradeRequest(reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }

    @Override
    @GetMapping("/requests/received")
    public ApiResponse<List<TradeResDTO.ReceivedRequestDTO>> getReceivedTradeRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.ReceivedRequestDTO> resDto = tradeService.getReceivedTradeRequests(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/requests/sent")
    public ApiResponse<List<TradeResDTO.SentRequestDTO>> getSentTradeRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.SentRequestDTO> resDto = tradeService.getSentTradeRequests(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @PatchMapping("/requests/{tradeRequestId}/approve")
    public ApiResponse<Void> approveTradeRequest(
            @PathVariable Long tradeRequestId,
            @RequestBody(required = false) @Valid TradeReqDTO.ApproveDTO reqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.approveTradeRequest(tradeRequestId, reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }

    @Override
    @GetMapping("/history/purchased")
    public ApiResponse<List<TradeResDTO.PurchasedTradeDTO>> getPurchasedTrades(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.PurchasedTradeDTO> resDto = tradeService.getPurchasedTrades(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @GetMapping("/history/sold")
    public ApiResponse<List<TradeResDTO.SoldTradeDTO>> getSoldTrades(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<TradeResDTO.SoldTradeDTO> resDto = tradeService.getSoldTrades(userDetails.getMember());
        return ApiResponse.ok(resDto);
    }

    @Override
    @PostMapping("/{tradeId}/reviews")
    public ApiResponse<Void> createTradeReview(
            @PathVariable Long tradeId,
            @RequestBody @Valid TradeReqDTO.ReviewCreateDTO reqDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        tradeService.createTradeReview(tradeId, reqDto, userDetails.getMember());
        return ApiResponse.ok();
    }
}
