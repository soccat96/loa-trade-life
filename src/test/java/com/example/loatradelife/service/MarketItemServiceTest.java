package com.example.loatradelife.service;

import com.example.loatradelife.domain.MarketItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MarketItemServiceTest {
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
        MarketItem marketItem = new MarketItem(
                65583,
                "name",
                3,
                2,
                "link",
                true
        );

        Long id = marketItemService.saveMarketItem(marketItem);

        assertThat(id).isEqualTo(1);
    }

    @Test
    void updateMarketItem() {
        MarketItem marketItem = new MarketItem(
                65583,
                "name",
                3,
                2,
                "link",
                true
        );
        MarketItem newMarketItem = new MarketItem(
                74302,
                "name_new",
                3,
                3,
                "link_new",
                true
        );

        Long id = marketItemService.saveMarketItem(marketItem);
        marketItemService.updateMarketItem(id, newMarketItem);
        Optional<MarketItem> oneMarketItem = marketItemService.findOneMarketItem(id);

        assertThat(oneMarketItem.isEmpty()).isFalse();
        MarketItem get = oneMarketItem.get();
        assertThat(get.getCode()).isEqualTo(74302);
        assertThat(get.getName()).isEqualTo("name_new");
        assertThat(get.getTier()).isEqualTo(3);
        assertThat(get.getGrade()).isEqualTo(3);
        assertThat(get.getImageLink()).isEqualTo("link_new");
        assertThat(get.getUseAt()).isTrue();
    }

    @Test
    void changeMarketItemUseAt() {
        MarketItem marketItem = new MarketItem(
                65583,
                "name",
                3,
                1,
                "link",
                true
        );

        Long id = marketItemService.saveMarketItem(marketItem);
        marketItemService.changeMarketItemUseAt(id, false);
        Optional<MarketItem> oneMarketItem = marketItemService.findOneMarketItem(id);

        assertThat(oneMarketItem.isEmpty()).isFalse();
        assertThat(oneMarketItem.get().getUseAt()).isFalse();
    }
}