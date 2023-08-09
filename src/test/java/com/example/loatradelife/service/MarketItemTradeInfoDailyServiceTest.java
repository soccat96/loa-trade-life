package com.example.loatradelife.service;

import com.example.loatradelife.domain.ItemGrade;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.domain.MarketItemTradeInfoDaily;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MarketItemTradeInfoDailyServiceTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveMarketItemTradeInfoDaily() {
        LocalDateTime now = LocalDateTime.now();
        ItemGrade normal = new ItemGrade(0,"일반");
        entityManager.persist(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                68447,
                "name",
                2,
                normal,
                "link"
        );
        entityManager.persist(marketItem);
        MarketItemTradeInfoDaily marketItemTradeInfoDaily = MarketItemTradeInfoDaily.builder()
                .marketItem(marketItem)
                .date(now)
                .avgPrice(16.9)
                .tradeCount(1_000_500)
                .build();

        Long id1 = marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(marketItemTradeInfoDaily);
        Long id2 = marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(marketItemTradeInfoDaily);

        assertThat(id1).isNotEqualTo(-1);
        assertThat(id2).isEqualTo(-1);
    }

    @Test
    void findOneMarketItemTradeInfoDaily() {
        LocalDateTime now = LocalDateTime.now();
        ItemGrade normal = new ItemGrade(0,"일반");
        entityManager.persist(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                68447,
                "name",
                2,
                normal,
                "link"
        );
        entityManager.persist(marketItem);
        MarketItemTradeInfoDaily marketItemTradeInfoDaily = MarketItemTradeInfoDaily.builder()
                .marketItem(marketItem)
                .date(now)
                .avgPrice(16.9)
                .tradeCount(1_000_500)
                .build();

        Long id = marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(marketItemTradeInfoDaily);
        entityManager.flush();
        Optional<MarketItemTradeInfoDaily> findById = marketItemTradeInfoDailyService.findOneMarketItemTradeInfoDaily(id);

        assertThat(findById.isPresent()).isTrue();
        MarketItemTradeInfoDaily get = findById.get();
        assertThat(get.getId()).isEqualTo(id);
        MarketItem mi = get.getMarketItem();
        ItemGrade ig = mi.getItemGrade();
        assertThat(ig.getId()).isEqualTo(normal.getId());
        assertThat(ig.getNumber()).isEqualTo(normal.getNumber());
        assertThat(ig.getName()).isEqualTo(normal.getName());
        assertThat(ig.getUseAt()).isEqualTo(normal.getUseAt());
        assertThat(mi.getId()).isEqualTo(marketItem.getId());
        assertThat(mi.getCategoryCode()).isEqualTo(marketItem.getCategoryCode());
        assertThat(mi.getCode()).isEqualTo(marketItem.getCode());
        assertThat(mi.getName()).isEqualTo(marketItem.getName());
        assertThat(mi.getTier()).isEqualTo(marketItem.getTier());
        assertThat(mi.getImageLink()).isEqualTo(marketItem.getImageLink());
        assertThat(mi.getUseAt()).isEqualTo(marketItem.getUseAt());
        assertThat(get.getDate().isEqual(now)).isTrue();
        assertThat(get.getAvgPrice()).isEqualTo(16.9);
        assertThat(get.getTradeCount()).isEqualTo(1_000_500);
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
        MarketItemTradeInfoDaily newOne = MarketItemTradeInfoDaily.builder()
                .marketItem(newMarketItem)
                .date(now.plusDays(1))
                .avgPrice(17.9)
                .tradeCount(1_000_600)
                .build();

        marketItemTradeInfoDailyService.updateMarketItemTradeInfoDaily(marketItemTradeInfoDaily.getId(), newOne);
        entityManager.flush();

        Optional<MarketItemTradeInfoDaily> findById = marketItemTradeInfoDailyService.findOneMarketItemTradeInfoDaily(marketItemTradeInfoDaily.getId());
        assertThat(findById.isPresent()).isTrue();
        MarketItemTradeInfoDaily get = findById.get();
        MarketItem mi = get.getMarketItem();
        ItemGrade ig = mi.getItemGrade();
        assertThat(ig.getId()).isEqualTo(uncommon.getId());
        assertThat(mi.getId()).isEqualTo(newMarketItem.getId());
        assertThat(get.getId()).isEqualTo(marketItemTradeInfoDaily.getId());
        assertThat(get.getDate().isEqual(now.plusDays(1))).isTrue();
        assertThat(get.getAvgPrice()).isEqualTo(17.9);
        assertThat(get.getTradeCount()).isEqualTo(1_000_600);
    }

    @Test
    void deleteMarketItemTradeInfoDaily() {
        LocalDateTime now = LocalDateTime.now();
        ItemGrade normal = new ItemGrade(0,"일반");
        entityManager.persist(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                68447,
                "name",
                2,
                normal,
                "link"
        );
        entityManager.persist(marketItem);
        MarketItemTradeInfoDaily marketItemTradeInfoDaily = MarketItemTradeInfoDaily.builder()
                .marketItem(marketItem)
                .date(now)
                .avgPrice(16.9)
                .tradeCount(1_000_500)
                .build();
        Long id = marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(marketItemTradeInfoDaily);

        marketItemTradeInfoDailyService.deleteMarketItemTradeInfoDaily(id);

        Optional<MarketItemTradeInfoDaily> findById = marketItemTradeInfoDailyService.findOneMarketItemTradeInfoDaily(id);
        assertThat(findById.isEmpty()).isTrue();
        MarketItem mi = entityManager.find(MarketItem.class, marketItem.getId());
        ItemGrade ig = entityManager.find(ItemGrade.class, normal.getId());
        assertThat(mi).isNotNull();
        assertThat(ig).isNotNull();
    }
}