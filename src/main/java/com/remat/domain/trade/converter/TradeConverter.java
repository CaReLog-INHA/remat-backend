package com.remat.domain.trade.converter;

import com.remat.domain.material.entity.Material;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeReview;
import com.remat.domain.trade.entity.TradeRequest;
import com.remat.domain.trade.entity.enums.RequestStatus;

public class TradeConverter {

    public static TradeRequest toEntity(TradeReqDTO.CreateDTO reqDto, Member member, Material material) {
        return TradeRequest.builder()
                .requestMember(member)
                .requestMaterial(material)
                .quantity(reqDto.quantity())
                .requestMessage(reqDto.requestMessage())
                .requestStatus(RequestStatus.PENDING)
                .rentalStart(reqDto.rentalStart())
                .rentalEnd(reqDto.rentalEnd())
                .build();
    }

    public static TradeReview toReviewEntity(TradeReqDTO.ReviewCreateDTO reqDto, Member reviewer, Member reviewee, Trade trade) {
        return TradeReview.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .trade(trade)
                .starRating(reqDto.starRating())
                .description(reqDto.description())
                .build();
    }

    public static Trade toTradeEntity(TradeRequest tradeRequest, Integer finalPrice) {
        Material material = tradeRequest.getRequestMaterial();

        return Trade.builder()
                .seller(material.getMember())
                .buyer(tradeRequest.getRequestMember())
                .tradeRequest(tradeRequest)
                .finalPrice(finalPrice)
                .rentalStart(tradeRequest.getRentalStart())
                .rentalEnd(tradeRequest.getRentalEnd())
                .build();
    }

    public static TradeResDTO.ReceivedRequestDTO toReceivedRequestDTO(TradeRequest tradeRequest, String imageUrl) {
        Material material = tradeRequest.getRequestMaterial();
        Member requester = tradeRequest.getRequestMember();

        return new TradeResDTO.ReceivedRequestDTO(
                tradeRequest.getId(),
                tradeRequest.getRequestStatus(),
                material.getId(),
                material.getMaterialName(),
                material.getPrice(),
                tradeRequest.getQuantity(),
                material.getUnit(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getRegion().getKoreanName(),
                requester.getId(),
                requester.getName(),
                requester.getCompanyName(),
                requester.getStarRating(),
                tradeRequest.getRequestMessage(),
                tradeRequest.getRentalStart(),
                tradeRequest.getRentalEnd(),
                tradeRequest.getCreatedAt()
        );
    }

    public static TradeResDTO.SentRequestDTO toSentRequestDTO(TradeRequest tradeRequest, String imageUrl) {
        Material material = tradeRequest.getRequestMaterial();
        Member seller = material.getMember();

        return new TradeResDTO.SentRequestDTO(
                tradeRequest.getId(),
                tradeRequest.getRequestStatus(),
                material.getId(),
                material.getMaterialName(),
                material.getPrice(),
                tradeRequest.getQuantity(),
                material.getUnit(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getRegion().getKoreanName(),
                seller.getId(),
                seller.getName(),
                seller.getCompanyName(),
                seller.getStarRating(),
                tradeRequest.getRequestMessage(),
                tradeRequest.getRentalStart(),
                tradeRequest.getRentalEnd(),
                tradeRequest.getCreatedAt()
        );
    }

    public static TradeResDTO.PurchasedTradeDTO toPurchasedTradeDTO(Trade trade, String imageUrl) {
        TradeRequest tradeRequest = trade.getTradeRequest();
        Material material = tradeRequest.getRequestMaterial();
        Member seller = trade.getSeller();

        return new TradeResDTO.PurchasedTradeDTO(
                trade.getId(),
                tradeRequest.getId(),
                material.getId(),
                material.getMaterialName(),
                trade.getFinalPrice(),
                tradeRequest.getQuantity(),
                material.getUnit(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getRegion().getKoreanName(),
                seller.getId(),
                seller.getName(),
                seller.getCompanyName(),
                seller.getStarRating(),
                trade.getRentalStart(),
                trade.getRentalEnd(),
                trade.getCreatedAt()
        );
    }

    public static TradeResDTO.SoldTradeDTO toSoldTradeDTO(Trade trade, String imageUrl) {
        TradeRequest tradeRequest = trade.getTradeRequest();
        Material material = tradeRequest.getRequestMaterial();
        Member buyer = trade.getBuyer();

        return new TradeResDTO.SoldTradeDTO(
                trade.getId(),
                tradeRequest.getId(),
                material.getId(),
                material.getMaterialName(),
                trade.getFinalPrice(),
                tradeRequest.getQuantity(),
                material.getUnit(),
                material.getTransactionType(),
                imageUrl,
                material.getCategory().getDisplayName(),
                material.getRegion().getKoreanName(),
                buyer.getId(),
                buyer.getName(),
                buyer.getCompanyName(),
                buyer.getStarRating(),
                trade.getRentalStart(),
                trade.getRentalEnd(),
                trade.getCreatedAt()
        );
    }
}
