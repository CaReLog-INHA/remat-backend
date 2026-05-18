package com.remat.domain.trade.repository;

import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {

    @Query("SELECT tr FROM TradeRequest tr " +
            "JOIN FETCH tr.requestMember rm " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.category " +
            "WHERE m.member = :owner " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "ORDER BY tr.createdAt DESC")
    List<TradeRequest> findReceivedRequestsByOwner(@Param("owner") Member owner);
}
