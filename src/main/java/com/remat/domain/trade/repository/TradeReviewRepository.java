package com.remat.domain.trade.repository;

import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview, Long> {

    boolean existsByTradeAndReviewer(Trade trade, Member reviewer);

    @Query("SELECT r FROM TradeReview r " +
            "JOIN FETCH r.reviewer " +
            "JOIN FETCH r.trade " +
            "WHERE r.reviewee.id = :memberId " +
            "ORDER BY r.createdAt DESC")
    List<TradeReview> findReceivedReviewsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT r FROM TradeReview r " +
            "JOIN FETCH r.reviewee " +
            "JOIN FETCH r.trade " +
            "WHERE r.reviewer.id = :memberId " +
            "ORDER BY r.createdAt DESC")
    List<TradeReview> findWrittenReviewsByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    long countByReviewerId(Long reviewerId);

    long countByRevieweeId(Long revieweeId);
}
