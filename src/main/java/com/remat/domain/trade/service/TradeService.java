package com.remat.domain.trade.service;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.converter.TradeConverter;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
import com.remat.domain.trade.entity.TradeRequest;
import com.remat.domain.trade.exception.TradeException;
import com.remat.domain.trade.exception.enums.TradeErrorCode;
import com.remat.domain.trade.repository.TradeRequestRepository;
import com.remat.domain.trade.repository.TradeRepository;
import com.remat.global.service.R2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeService {

    private final TradeRequestRepository tradeRequestRepository;
    private final TradeRepository tradeRepository;
    private final MaterialRepository materialRepository;
    private final R2Service r2Service;

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

    public List<TradeResDTO.ReceivedRequestDTO> getReceivedTradeRequests(Member member) {
        return tradeRequestRepository.findReceivedRequestsByOwner(member).stream()
                .map(tradeRequest -> TradeConverter.toReceivedRequestDTO(
                        tradeRequest,
                        r2Service.getFileUrl(tradeRequest.getRequestMaterial().getImageKey())
                ))
                .toList();
    }

    public List<TradeResDTO.SentRequestDTO> getSentTradeRequests(Member member) {
        return tradeRequestRepository.findSentRequestsByRequester(member).stream()
                .map(tradeRequest -> TradeConverter.toSentRequestDTO(
                        tradeRequest,
                        r2Service.getFileUrl(tradeRequest.getRequestMaterial().getImageKey())
                ))
                .toList();
    }

    public List<TradeResDTO.PurchasedTradeDTO> getPurchasedTrades(Member member) {
        return tradeRepository.findPurchasedTradesByBuyer(member).stream()
                .map(trade -> TradeConverter.toPurchasedTradeDTO(
                        trade,
                        r2Service.getFileUrl(trade.getTradeRequest().getRequestMaterial().getImageKey())
                ))
                .toList();
    }
}
