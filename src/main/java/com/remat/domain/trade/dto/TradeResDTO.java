package com.remat.domain.trade.dto;

import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.trade.entity.enums.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TradeResDTO {

    public record ReceivedRequestDTO(
            Long tradeRequestId,
            RequestStatus requestStatus,
            Long materialId,
            String materialName,
            Integer materialPrice,
            Integer requestedQuantity,
            Integer unit,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            Long requesterId,
            String requesterName,
            String requesterCompanyName,
            Integer requesterStarRating,
            String requestMessage,
            LocalDate rentalStart,
            LocalDate rentalEnd,
            LocalDateTime createdAt
    ) {}

    public record SentRequestDTO(
            Long tradeRequestId,
            RequestStatus requestStatus,
            Long materialId,
            String materialName,
            Integer materialPrice,
            Integer requestedQuantity,
            Integer unit,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            Long sellerId,
            String sellerName,
            String sellerCompanyName,
            Integer sellerStarRating,
            String requestMessage,
            LocalDate rentalStart,
            LocalDate rentalEnd,
            LocalDateTime createdAt
    ) {}

    public record PurchasedTradeDTO(
            Long tradeId,
            Long tradeRequestId,
            Long materialId,
            String materialName,
            Integer finalPrice,
            Integer requestedQuantity,
            Integer unit,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            Long sellerId,
            String sellerName,
            String sellerCompanyName,
            Integer sellerStarRating,
            LocalDate rentalStart,
            LocalDate rentalEnd,
            LocalDateTime createdAt
    ) {}

    public record SoldTradeDTO(
            Long tradeId,
            Long tradeRequestId,
            Long materialId,
            String materialName,
            Integer finalPrice,
            Integer requestedQuantity,
            Integer unit,
            TransactionType transactionType,
            String imageUrl,
            String categoryName,
            String region,
            Long buyerId,
            String buyerName,
            String buyerCompanyName,
            Integer buyerStarRating,
            LocalDate rentalStart,
            LocalDate rentalEnd,
            LocalDateTime createdAt
    ) {}
}
