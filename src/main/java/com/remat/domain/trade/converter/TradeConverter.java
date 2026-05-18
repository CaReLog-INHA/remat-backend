package com.remat.domain.trade.converter;

import com.remat.domain.material.entity.Material;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
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
}
