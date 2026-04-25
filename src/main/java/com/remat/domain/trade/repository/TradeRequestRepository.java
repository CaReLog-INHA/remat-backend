package com.remat.domain.trade.repository;

import com.remat.domain.trade.entity.TradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRequestRepository extends JpaRepository<TradeRequest, Long> {
}
