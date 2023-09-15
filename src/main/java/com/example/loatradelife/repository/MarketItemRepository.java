package com.example.loatradelife.repository;

import com.example.loatradelife.domain.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketItemRepository extends JpaRepository<MarketItem, Long> {
    List<MarketItem> findMarketItemsByCategoryCode(Integer categoryCode);
}