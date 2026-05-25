package com.remat.domain.trade.repository;

import com.remat.domain.member.entity.Member;
import com.remat.domain.trade.entity.Trade;
import com.remat.domain.trade.entity.TradeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview, Long> {

    boolean existsByTradeAndReviewer(Trade trade, Member reviewer);
}
