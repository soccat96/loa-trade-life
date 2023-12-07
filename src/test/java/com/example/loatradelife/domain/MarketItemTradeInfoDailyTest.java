package com.example.loatradelife.domain;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MarketItemTradeInfoDailyTest {
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateMarketItemTradeInfoDaily() {
        LocalDateTime now = LocalDateTime.now();
        ItemGrade normal = new ItemGrade(0,"일반");
        ItemGrade uncommon = new ItemGrade(1,"고급");
        entityManager.persist(normal);
        entityManager.persist(uncommon);
        MarketItem marketItem = new MarketItem(
                90200,
                68447,
                "name",
                2,
                normal,
                "link"
        );
        MarketItem newMarketItem = new MarketItem(
                90200,
                68448,
                "name_new",
                3,
                uncommon,
                "link_new"
        );
        entityManager.persist(marketItem);
        entityManager.persist(newMarketItem);
        MarketItemTradeInfoDaily marketItemTradeInfoDaily = MarketItemTradeInfoDaily.builder()
                .marketItem(marketItem)
                .date(now)
                .avgPrice(16.9)
                .tradeCount(1_000_500)
                .build();
        entityManager.persist(marketItemTradeInfoDaily);

        MarketItemTradeInfoDaily newOne = new MarketItemTradeInfoDaily(
                newMarketItem,
                now.plusDays(1),
                17.9,
                1_000_600,
                10
        );
        MarketItemTradeInfoDaily.builder()
                .marketItem(newMarketItem)
                .date(now.plusDays(1))
                .avgPrice(17.9)
                .tradeCount(1_000_600)
                .build();
        marketItemTradeInfoDaily.update(newOne);
        entityManager.flush();

        MarketItemTradeInfoDaily findById = entityManager.find(MarketItemTradeInfoDaily.class, marketItemTradeInfoDaily.getId());
        assertThat(findById).isNotNull();
        assertThat(findById.getMarketItem().getItemGrade().getId()).isEqualTo(uncommon.getId());
        assertThat(findById.getMarketItem().getId()).isEqualTo(newMarketItem.getId());
        assertThat(findById.getDate().isEqual(now.plusDays(1))).isTrue();
        assertThat(findById.getAvgPrice()).isEqualTo(17.9);
        assertThat(findById.getTradeCount()).isEqualTo(1_000_600);
    }
}