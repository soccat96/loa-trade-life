package com.example.loatradelife.service;

import com.example.loatradelife.domain.ItemGrade;
import com.example.loatradelife.domain.MarketItem;
import com.example.loatradelife.repository.ItemGradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MarketItemServiceTest {
    @Autowired
    private ItemGradeRepository itemGradeRepository;
    @Autowired
    private MarketItemService marketItemService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveMarketItem() {
        ItemGrade normal = new ItemGrade(0,"일반");
        itemGradeRepository.save(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                65583,
                "name",
                3,
                normal,
                "link"
        );

        Long id = marketItemService.saveMarketItem(marketItem);

        assertThat(id).isNotEqualTo(-1);
    }

    @Test
    void updateMarketItem() {
        ItemGrade normal = new ItemGrade(0,"일반");
        ItemGrade uncommon = new ItemGrade(1,"고급");
        itemGradeRepository.save(normal);
        itemGradeRepository.save(uncommon);
        MarketItem marketItem = new MarketItem(
                90200,
                65583,
                "name",
                3,
                normal,
                "link"
        );
        MarketItem newMarketItem = new MarketItem(
                90300,
                74302,
                "name_new",
                3,
                uncommon,
                "link_new"
        );

        Long id = marketItemService.saveMarketItem(marketItem);
        marketItemService.updateMarketItem(id, newMarketItem);
        Optional<MarketItem> oneMarketItem = marketItemService.findOneMarketItem(id);

        assertThat(oneMarketItem.isEmpty()).isFalse();
        MarketItem get = oneMarketItem.get();
        assertThat(get.getCategoryCode()).isEqualTo(90300);
        assertThat(get.getCode()).isEqualTo(74302);
        assertThat(get.getName()).isEqualTo("name_new");
        assertThat(get.getTier()).isEqualTo(3);
        assertThat(get.getItemGrade()).isEqualTo(uncommon);
        assertThat(get.getImageLink()).isEqualTo("link_new");
        assertThat(get.getUseAt()).isTrue();
    }

    @Test
    void changeMarketItemUseAt() {
        ItemGrade normal = new ItemGrade(0,"일반");
        itemGradeRepository.save(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                65583,
                "name",
                3,
                normal,
                "link"
        );

        Long id = marketItemService.saveMarketItem(marketItem);
        marketItemService.changeMarketItemUseAt(id, false);
        Optional<MarketItem> oneMarketItem = marketItemService.findOneMarketItem(id);

        assertThat(oneMarketItem.isEmpty()).isFalse();
        assertThat(oneMarketItem.get().getUseAt()).isFalse();
    }

    @Test
    public void getMarketItemByCategoryCode() {
        ItemGrade normal = new ItemGrade(0,"일반");
        itemGradeRepository.save(normal);
        MarketItem marketItem0 = new MarketItem(
                90200,
                60000,
                "name0",
                3,
                normal,
                "link0"
        );
        MarketItem marketItem1 = new MarketItem(
                90200,
                60001,
                "name1",
                3,
                normal,
                "link1"
        );
        MarketItem marketItem2 = new MarketItem(
                90300,
                60002,
                "name3",
                3,
                normal,
                "link3"
        );
        marketItemService.saveMarketItem(marketItem0);
        marketItemService.saveMarketItem(marketItem1);
        marketItemService.saveMarketItem(marketItem2);

        List<MarketItem> list = marketItemService.findListMarketItemByCategoryCode(90200);

        assertThat(list.size()).isEqualTo(2);
    }
}