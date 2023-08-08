package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_item_id")
    private Long id;

    private Integer categoryCode;
    private Integer code;
    private String name;
    private Integer tier;
    private String imageLink;
    private Boolean useAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number")
    private ItemGrade itemGrade;

    public MarketItem(Integer categoryCode, Integer code, String name, Integer tier, ItemGrade itemGrade, String imageLink) {
        this.categoryCode = categoryCode;
        this.code = code;
        this.name = name;
        this.tier = tier;
        this.itemGrade = itemGrade;
        this.imageLink = imageLink;
        this.useAt = true;
    }

    public MarketItem(Integer categoryCode, Integer code, String name, Integer tier, ItemGrade itemGrade, String imageLink, Boolean useAt) {
        this.categoryCode = categoryCode;
        this.code = code;
        this.name = name;
        this.tier = tier;
        this.itemGrade = itemGrade;
        this.imageLink = imageLink;
        this.useAt = useAt;
    }

    public void updateMarketItem(MarketItem marketItem) {
        this.categoryCode = marketItem.getCategoryCode();
        this.code = marketItem.getCode();
        this.name = marketItem.getName();
        this.tier = marketItem.getTier();
        this.itemGrade = marketItem.getItemGrade();
        this.imageLink = marketItem.getImageLink();
        this.useAt = marketItem.getUseAt();
    }

    public void changeUseAt(boolean useAt) {
        this.useAt = useAt;
    }
}