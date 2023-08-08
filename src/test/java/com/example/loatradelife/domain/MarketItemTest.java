package com.example.loatradelife.domain;

import com.example.loatradelife.repository.ItemGradeRepository;
import com.example.loatradelife.repository.MarketItemRepository;
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
class MarketItemTest {
    @Autowired
    private ItemGradeRepository itemGradeRepository;
    @Autowired
    private MarketItemRepository marketItemRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateMarketItem() {
        ItemGrade normal = new ItemGrade(0,"일반");
        ItemGrade uncommon = new ItemGrade(1,"고급");
        itemGradeRepository.save(normal);
        itemGradeRepository.save(uncommon);
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
        marketItemRepository.save(marketItem);

        marketItem.updateMarketItem(newMarketItem);
        Optional<MarketItem> findById = marketItemRepository.findById(marketItem.getId());

        assertThat(findById.isPresent()).isTrue();
        MarketItem get = findById.get();
        assertThat(get.getCode()).isEqualTo(68448);
        assertThat(get.getName()).isEqualTo("name_new");
        assertThat(get.getTier()).isEqualTo(3);
        assertThat(get.getItemGrade()).isEqualTo(uncommon);
        assertThat(get.getUseAt()).isTrue();
    }

    @Test
    void changeUseAt() {
        ItemGrade normal = new ItemGrade(0,"일반");
        itemGradeRepository.save(normal);
        MarketItem marketItem = new MarketItem(
                90200,
                68447,
                "name",
                2,
                normal,
                "link",
                false
        );
        marketItemRepository.save(marketItem);

        marketItem.changeUseAt(true);
        Optional<MarketItem> findById = marketItemRepository.findById(marketItem.getId());

        assertThat(findById.isPresent()).isTrue();
        MarketItem get = findById.get();
        assertThat(get.getUseAt()).isTrue();
    }
}