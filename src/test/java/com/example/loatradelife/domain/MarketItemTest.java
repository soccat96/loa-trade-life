package com.example.loatradelife.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarketItemTest {
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateMarketItem() {
        MarketItem marketItem = new MarketItem(
                68447,
                "name",
                2,
                1,
                "link",
                false
        );
        MarketItem newMarketItem = new MarketItem(
                68448,
                "name_new",
                3,
                2,
                "link_new",
                true
        );

        marketItem.updateMarketItem(newMarketItem);

        assertThat(marketItem.getCode()).isEqualTo(68448);
        assertThat(marketItem.getName()).isEqualTo("name_new");
        assertThat(marketItem.getTier()).isEqualTo(3);
        assertThat(marketItem.getGrade()).isEqualTo(2);
        assertThat(marketItem.getUseAt()).isTrue();
    }

    @Test
    void changeUseAt() {
        MarketItem marketItem = new MarketItem(
                68447,
                "name",
                2,
                1,
                "link",
                false
        );

        marketItem.changeUseAt(true);

        assertThat(marketItem.getUseAt()).isTrue();
    }
}