package com.remat.domain.trade.repository;

import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    @Query("SELECT t FROM Trade t " +
            "JOIN FETCH t.seller s " +
            "JOIN FETCH t.buyer b " +
            "JOIN FETCH t.tradeRequest tr " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.category " +
            "WHERE t.buyer = :buyer " +
            "AND t.deletedAt IS NULL " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "ORDER BY t.createdAt DESC")
    List<Trade> findPurchasedTradesByBuyer(@Param("buyer") Member buyer);

    @Query("SELECT t FROM Trade t " +
            "JOIN FETCH t.seller s " +
            "JOIN FETCH t.buyer b " +
            "JOIN FETCH t.tradeRequest tr " +
            "JOIN FETCH tr.requestMaterial m " +
            "JOIN FETCH m.category " +
            "WHERE t.seller = :seller " +
            "AND t.deletedAt IS NULL " +
            "AND tr.deletedAt IS NULL " +
            "AND m.deletedAt IS NULL " +
            "ORDER BY t.createdAt DESC")
    List<Trade> findSoldTradesBySeller(@Param("seller") Member seller);

    @Query("SELECT t FROM Trade t " +
            "JOIN FETCH t.seller s " +
            "JOIN FETCH t.buyer b " +
            "WHERE t.id = :tradeId " +
            "AND t.deletedAt IS NULL")
    Optional<Trade> findByIdAndDeletedAtIsNullWithMembers(@Param("tradeId") Long tradeId);
}
