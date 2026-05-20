package com.remat.domain.trade.service;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.converter.TradeConverter;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.entity.TradeRequest;
import com.remat.domain.trade.exception.TradeException;
import com.remat.domain.trade.exception.enums.TradeErrorCode;
import com.remat.domain.trade.repository.TradeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRequestRepository tradeRequestRepository;
    private final MaterialRepository materialRepository;

    @Transactional
    public void createTradeRequest(TradeReqDTO.CreateDTO reqDto, Member member) {
        Material material = materialRepository.findByIdAndDeletedAtIsNull(reqDto.materialId())
                .orElseThrow(() -> new TradeException(TradeErrorCode.MATERIAL_NOT_FOUND));

        if (material.getMember().getId().equals(member.getId())) {
            throw new TradeException(TradeErrorCode.CANNOT_REQUEST_OWN_MATERIAL);
        }

        if (reqDto.quantity() > material.getQuantity()) {
            throw new TradeException(TradeErrorCode.QUANTITY_EXCEEDS_STOCK);
        }

        if (material.getTransactionType() == TransactionType.RENTAL) {
            if (reqDto.rentalStart() == null || reqDto.rentalEnd() == null) {
                throw new TradeException(TradeErrorCode.RENTAL_DATE_REQUIRED);
            }
            if (!reqDto.rentalEnd().isAfter(reqDto.rentalStart())) {
                throw new TradeException(TradeErrorCode.INVALID_RENTAL_DATE);
            }
        }

        TradeRequest tradeRequest = TradeConverter.toEntity(reqDto, member, material);
        tradeRequestRepository.save(tradeRequest);
    }
}
