package com.remat.domain.trade.converter;

import com.remat.domain.material.entity.Material;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.dto.TradeReqDTO;
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
}
