package com.example.loatradelife.controller.dto;

import com.example.loatradelife.domain.ItemGrade;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketItemTradeInfoDailyDto {
    private Integer number;
    private String itemGradeName;

    private Integer categoryCode;
    private Integer code;
    private String marketItemName;
    private Integer tier;
    private String imageLink;

    private Long id;
    private LocalDateTime date;
    private Double avgPrice;
    private Integer tradeCount;
    private Integer bundleCount;

    public MarketItemTradeInfoDailyDto(MarketItemTradeInfoDaily marketItemTradeInfoDaily) {
        MarketItem marketItem = marketItemTradeInfoDaily.getMarketItem();
        ItemGrade itemGrade = marketItem.getItemGrade();

        this.number = itemGrade.getNumber();
        this.itemGradeName = itemGrade.getName();

        this.categoryCode = marketItem.getCategoryCode();
        this.code = marketItem.getCode();
        this.marketItemName = marketItem.getName();
        this.tier = marketItem.getTier();
        this.imageLink = marketItem.getImageLink();

        this.id = marketItemTradeInfoDaily.getId();
        this.date = marketItemTradeInfoDaily.getDate();
        this.avgPrice = marketItemTradeInfoDaily.getAvgPrice();
        this.tradeCount = marketItemTradeInfoDaily.getTradeCount();
        this.bundleCount = marketItemTradeInfoDaily.getBundleCount();
    }
}
