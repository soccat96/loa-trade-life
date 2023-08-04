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

    private Integer code;
    private String name;
    private Integer tier = 0;
    private Integer grade;
    private String imageLink;
    private Boolean useAt;

    public MarketItem(Integer code, String name, Integer tier, Integer grade, String imageLink, Boolean useAt) {
        this.code = code;
        this.name = name;
        this.tier = tier;
        this.grade = grade;
        this.imageLink = imageLink;
        this.useAt = useAt;
    }

    public void updateMarketItem(MarketItem marketItem) {
        this.code = marketItem.getCode();
        this.name = marketItem.getName();
        this.tier = marketItem.getTier();
        this.grade = marketItem.getGrade();
        this.imageLink = marketItem.getImageLink();
        this.useAt = marketItem.getUseAt();
    }

    public void changeUseAt(boolean useAt) {
        this.useAt = useAt;
    }
}