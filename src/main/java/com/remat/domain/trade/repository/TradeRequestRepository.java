package com.remat.domain.trade.repository;

import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    @Query("SELECT tr FROM TradeRequest tr " +
            "JOIN FETCH tr.requestMember rm " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.category " +
            "WHERE m.member = :owner " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND tr.requestStatus = com.remat.domain.trade.entity.enums.RequestStatus.PENDING " +
            "ORDER BY tr.createdAt DESC")
    List<TradeRequest> findReceivedRequestsByOwner(@Param("owner") Member owner);

    @Query("SELECT tr FROM TradeRequest tr " +
            "JOIN FETCH tr.requestMember rm " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.member seller " +
            "JOIN FETCH m.category " +
            "WHERE tr.requestMember = :requester " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "AND tr.requestStatus = com.remat.domain.trade.entity.enums.RequestStatus.PENDING " +
            "ORDER BY tr.createdAt DESC")
    List<TradeRequest> findSentRequestsByRequester(@Param("requester") Member requester);

    @Query("SELECT tr FROM TradeRequest tr " +
            "JOIN FETCH tr.requestMember rm " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.member owner " +
            "WHERE tr.id = :tradeRequestId " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL")
    Optional<TradeRequest> findByIdAndDeletedAtIsNullWithMembers(@Param("tradeRequestId") Long tradeRequestId);
}
