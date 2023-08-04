package com.example.loatradelife.repository;

import com.example.loatradelife.domain.MarketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketItemRepository extends JpaRepository<MarketItem, Long> {
}