package com.remat.domain.trade.service;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.entity.enums.TransactionType;
import com.remat.domain.material.repository.MaterialRepository;
import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.converter.TradeConverter;
import com.remat.domain.trade.dto.TradeReqDTO;
import com.remat.domain.trade.dto.TradeResDTO;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeRequest;
import com.remat.domain.trade.entity.enums.RequestStatus;
import com.remat.domain.trade.exception.TradeException;
import com.remat.domain.trade.exception.enums.TradeErrorCode;
import com.remat.domain.trade.repository.TradeRequestRepository;
import com.remat.domain.trade.repository.TradeReviewRepository;
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
    private final TradeReviewRepository tradeReviewRepository;
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

    @Transactional
    public void approveTradeRequest(Long tradeRequestId, TradeReqDTO.ApproveDTO reqDto, Member owner) {
        TradeRequest tradeRequest = tradeRequestRepository.findByIdAndDeletedAtIsNullWithMembers(tradeRequestId)
                .orElseThrow(() -> new TradeException(TradeErrorCode.TRADE_REQUEST_NOT_FOUND));
        Material material = tradeRequest.getRequestMaterial();

        if (!material.getMember().getId().equals(owner.getId())) {
            throw new TradeException(TradeErrorCode.NOT_MATERIAL_OWNER);
        }
        if (tradeRequest.getRequestStatus() != RequestStatus.PENDING) {
            throw new TradeException(TradeErrorCode.TRADE_REQUEST_NOT_PENDING);
        }
        if (tradeRepository.existsByTradeRequest(tradeRequest)) {
            throw new TradeException(TradeErrorCode.TRADE_ALREADY_EXISTS);
        }
        if (tradeRequest.getQuantity() > material.getQuantity()) {
            throw new TradeException(TradeErrorCode.QUANTITY_EXCEEDS_STOCK);
        }

        Integer finalPrice = reqDto != null && reqDto.finalPrice() != null
                ? reqDto.finalPrice()
                : material.getPrice();

        tradeRequest.accept();
        material.decreaseQuantity(tradeRequest.getQuantity());
        tradeRepository.save(TradeConverter.toTradeEntity(tradeRequest, finalPrice));
    }

    @Transactional
    public void rejectTradeRequest(Long tradeRequestId, Member owner) {
        TradeRequest tradeRequest = tradeRequestRepository.findByIdAndDeletedAtIsNullWithMembers(tradeRequestId)
                .orElseThrow(() -> new TradeException(TradeErrorCode.TRADE_REQUEST_NOT_FOUND));
        Material material = tradeRequest.getRequestMaterial();

        if (!material.getMember().getId().equals(owner.getId())) {
            throw new TradeException(TradeErrorCode.NOT_MATERIAL_OWNER);
        }
        if (tradeRequest.getRequestStatus() != RequestStatus.PENDING) {
            throw new TradeException(TradeErrorCode.TRADE_REQUEST_NOT_PENDING);
        }

        tradeRequest.reject();
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

    public List<TradeResDTO.SoldTradeDTO> getSoldTrades(Member member) {
        return tradeRepository.findSoldTradesBySeller(member).stream()
                .map(trade -> TradeConverter.toSoldTradeDTO(
                        trade,
                        r2Service.getFileUrl(trade.getTradeRequest().getRequestMaterial().getImageKey())
                ))
                .toList();
    }

    @Transactional
    public void createTradeReview(Long tradeId, TradeReqDTO.ReviewCreateDTO reqDto, Member reviewer) {
        Trade trade = tradeRepository.findByIdAndDeletedAtIsNullWithMembers(tradeId)
                .orElseThrow(() -> new TradeException(TradeErrorCode.TRADE_NOT_FOUND));

        Member reviewee = getReviewee(trade, reviewer);

        if (tradeReviewRepository.existsByTradeAndReviewer(trade, reviewer)) {
            throw new TradeException(TradeErrorCode.ALREADY_REVIEWED);
        }

        tradeReviewRepository.save(TradeConverter.toReviewEntity(reqDto, reviewer, reviewee, trade));
    }

    private Member getReviewee(Trade trade, Member reviewer) {
        if (trade.getBuyer().getId().equals(reviewer.getId())) {
            return trade.getSeller();
        }
        if (trade.getSeller().getId().equals(reviewer.getId())) {
            return trade.getBuyer();
        }
        throw new TradeException(TradeErrorCode.NOT_TRADE_PARTICIPANT);
    }
}
