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
import java.util.List;
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

    @Test
    public void findListByDateBetween() {
        ItemGrade itemGrade = new ItemGrade(0,"일반");
        MarketItem marketItem = new MarketItem(
                90200,
                68448,
                "name",
                3,
                itemGrade,
                "link"
        );
        entityManager.persist(itemGrade);
        entityManager.persist(marketItem);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ed = now.withHour(23).withMinute(59).withSecond(59).withNano(0);
        LocalDateTime sd = ed.minusDays(5);
        MarketItemTradeInfoDaily data0 = new MarketItemTradeInfoDaily(marketItem, sd.minusDays(2), 10.0, 100, 10);
        MarketItemTradeInfoDaily data1 = new MarketItemTradeInfoDaily(marketItem, sd.minusDays(1), 10.1, 101, 10);
        MarketItemTradeInfoDaily data2 = new MarketItemTradeInfoDaily(marketItem, sd.minusDays(0), 10.2, 102, 10);
        MarketItemTradeInfoDaily data3 = new MarketItemTradeInfoDaily(marketItem, ed.minusDays(4), 10.3, 103, 10);
        MarketItemTradeInfoDaily data4 = new MarketItemTradeInfoDaily(marketItem, ed.minusDays(3), 10.4, 104, 10);
        MarketItemTradeInfoDaily data5 = new MarketItemTradeInfoDaily(marketItem, ed.minusDays(2), 10.5, 105, 10);
        MarketItemTradeInfoDaily data6 = new MarketItemTradeInfoDaily(marketItem, ed.minusDays(1), 10.6, 106, 10);
        MarketItemTradeInfoDaily data7 = new MarketItemTradeInfoDaily(marketItem, ed.minusDays(0), 10.7, 107, 10);
        MarketItemTradeInfoDaily data8 = new MarketItemTradeInfoDaily(marketItem, ed.plusDays(1),  10.8, 108, 10);
        MarketItemTradeInfoDaily data9 = new MarketItemTradeInfoDaily(marketItem, ed.plusDays(2),  10.9, 109, 10);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data0);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data1);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data2);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data3);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data4);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data5);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data6);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data7);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data8);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data9);

        List<MarketItemTradeInfoDaily> list = marketItemTradeInfoDailyService.findListByDateBetween(sd, ed);

        assertThat(list.size()).isEqualTo(6);
        MarketItemTradeInfoDaily dataFirst = list.get(0);
        MarketItemTradeInfoDaily dataLast = list.get(list.size() - 1);
        assertThat(dataFirst.getAvgPrice()).isEqualTo(10.2);
        assertThat(dataLast.getAvgPrice()).isEqualTo(10.7);
    }

    @Test
    public void findListByMarketItem() {
        ItemGrade itemGrade = new ItemGrade(0,"일반");
        MarketItem marketItem1 = new MarketItem(
                90200,
                68448,
                "name1",
                3,
                itemGrade,
                "link1"
        );
        MarketItem marketItem2 = new MarketItem(
                90300,
                78448,
                "name2",
                3,
                itemGrade,
                "link2"
        );
        entityManager.persist(itemGrade);
        entityManager.persist(marketItem1);
        entityManager.persist(marketItem2);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ed = now.withHour(23).withMinute(59).withSecond(59).withNano(0);
        LocalDateTime sd = ed.minusDays(5);
        MarketItemTradeInfoDaily data0 = new MarketItemTradeInfoDaily(marketItem1, sd.minusDays(2), 10.0, 100, 10);
        MarketItemTradeInfoDaily data1 = new MarketItemTradeInfoDaily(marketItem1, sd.minusDays(1), 10.1, 101, 10);
        MarketItemTradeInfoDaily data2 = new MarketItemTradeInfoDaily(marketItem1, sd.minusDays(0), 10.2, 102, 10);
        MarketItemTradeInfoDaily data3 = new MarketItemTradeInfoDaily(marketItem1, ed.minusDays(4), 10.3, 103, 10);
        MarketItemTradeInfoDaily data4 = new MarketItemTradeInfoDaily(marketItem1, ed.minusDays(3), 10.4, 104, 10);
        MarketItemTradeInfoDaily data5 = new MarketItemTradeInfoDaily(marketItem2, ed.minusDays(2), 10.5, 105, 10);
        MarketItemTradeInfoDaily data6 = new MarketItemTradeInfoDaily(marketItem2, ed.minusDays(1), 10.6, 106, 10);
        MarketItemTradeInfoDaily data7 = new MarketItemTradeInfoDaily(marketItem2, ed.minusDays(0), 10.7, 107, 10);
        MarketItemTradeInfoDaily data8 = new MarketItemTradeInfoDaily(marketItem2, ed.plusDays(1),  10.8, 108, 10);
        MarketItemTradeInfoDaily data9 = new MarketItemTradeInfoDaily(marketItem2, ed.plusDays(2),  10.9, 109, 10);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data0);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data1);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data2);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data3);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data4);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data5);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data6);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data7);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data8);
        marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data9);

        List<MarketItemTradeInfoDaily> dataList = marketItemTradeInfoDailyService.findListByMarketItemAndDateBetween(marketItem1, sd, ed);

        assertThat(dataList.size()).isEqualTo(3);
        MarketItem miFirst = dataList.get(0).getMarketItem();
        MarketItem miLast = dataList.get(dataList.size() - 1).getMarketItem();
        assertThat(miFirst.getId()).isEqualTo(1);
        assertThat(miFirst.getCategoryCode()).isEqualTo(90200);
        assertThat(miFirst.getName()).isEqualTo("name1");
        assertThat(miLast.getId()).isEqualTo(1);
        assertThat(miLast.getCategoryCode()).isEqualTo(90200);
        assertThat(miLast.getName()).isEqualTo("name1");
    }
}