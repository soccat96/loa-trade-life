package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketItemTradeInfoDaily {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_item_trade_info_daily_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_item_id")
    private MarketItem marketItem;
    private LocalDateTime date;
    private Double avgPrice;
    private Integer tradeCount;
    private Integer bundleCount;

    @Builder
    public MarketItemTradeInfoDaily(MarketItem marketItem, LocalDateTime date, Double avgPrice, Integer tradeCount, Integer bundleCount) {
        this.marketItem = marketItem;
        this.date = date;
        this.avgPrice = avgPrice;
        this.tradeCount = tradeCount;
        this.bundleCount = bundleCount;
    }

    public void updateMarketItemTradeInfoDaily(MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
        this.marketItem = marketItemTradeInfoDaily.getMarketItem();
        this.date = marketItemTradeInfoDaily.getDate();
        this.avgPrice = marketItemTradeInfoDaily.getAvgPrice();
        this.tradeCount = marketItemTradeInfoDaily.getTradeCount();
        this.bundleCount = marketItemTradeInfoDaily.getBundleCount();
    }
}
