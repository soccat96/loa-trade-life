package com.example.loatradelife.controller.dto;

import com.example.loatradelife.domain.ItemGrade;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketItemDto {
    private Integer number;
    private String itemGradeName;

    private Long id;
    private Integer categoryCode;
    private Integer code;
    private String name;
    private Integer tier;
    private String imageLink;
    private Boolean useAt;

    private List<MarketItemTradeInfoDailyDto> dataList = new ArrayList<>();

    public MarketItemDto(MarketItem marketItem) {
        ItemGrade itemGrade = marketItem.getItemGrade();
        this.number = itemGrade.getNumber();
        this.itemGradeName = itemGrade.getName();

        this.id = marketItem.getId();
        this.categoryCode = marketItem.getCategoryCode();
        this.code = marketItem.getCode();
        this.name = marketItem.getName();
        this.tier = marketItem.getTier();
        this.imageLink = marketItem.getImageLink();
        this.useAt = marketItem.getUseAt();
    }

    public MarketItemDto(MarketItem marketItem, List<MarketItemTradeInfoDaily> dataList) {
        ItemGrade itemGrade = marketItem.getItemGrade();
        this.number = itemGrade.getNumber();
        this.itemGradeName = itemGrade.getName();

        this.id = marketItem.getId();
        this.categoryCode = marketItem.getCategoryCode();
        this.code = marketItem.getCode();
        this.name = marketItem.getName();
        this.tier = marketItem.getTier();
        this.imageLink = marketItem.getImageLink();
        this.useAt = marketItem.getUseAt();

        for (MarketItemTradeInfoDaily x : dataList) {
            this.dataList.add(new MarketItemTradeInfoDailyDto(x));
        }
    }
}
