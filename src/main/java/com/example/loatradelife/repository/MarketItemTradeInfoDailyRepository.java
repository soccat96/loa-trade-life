package com.example.loatradelife.repository;

import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MarketItemTradeInfoDailyRepository extends JpaRepository<MarketItemTradeInfoDaily, Long> {
    boolean existsMarketItemTradeInfoDailyByMarketItemAndDate(MarketItem marketItem, LocalDateTime date);
}
