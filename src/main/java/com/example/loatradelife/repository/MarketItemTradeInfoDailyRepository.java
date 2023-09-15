package com.example.loatradelife.repository;

import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarketItemTradeInfoDailyRepository extends JpaRepository<MarketItemTradeInfoDaily, Long> {
    Optional<MarketItemTradeInfoDaily> findByMarketItemAndDate(MarketItem marketItem, LocalDateTime date);
    List<MarketItemTradeInfoDaily> findByDateBetweenOrderByDate(LocalDateTime startDate, LocalDateTime endDate);
    List<MarketItemTradeInfoDaily> findByMarketItemAndDateBetweenOrderByDate(MarketItem marketItem, LocalDateTime startDate, LocalDateTime endDate);
}
