package com.remat.domain.trade.repository;

import com.remat.domain.trade.entity.TradeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview, Long> {
}
