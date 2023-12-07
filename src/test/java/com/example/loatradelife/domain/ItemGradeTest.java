package com.example.loatradelife.domain;

import com.example.loatradelife.repository.ItemGradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemGradeTest {
    @Autowired
    private ItemGradeRepository itemGradeRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateItemGrade() {
        ItemGrade itemGrade = new ItemGrade(
                12345,
                "itemGrade"
        );
        ItemGrade itemGradeNew = new ItemGrade(
                123456,
                "itemGradeNew"
        );
        itemGradeRepository.save(itemGrade);

        itemGrade.update(itemGradeNew);
        Optional<ItemGrade> findById = itemGradeRepository.findById(itemGrade.getId());

        assertThat(findById.isPresent()).isTrue();
        ItemGrade get = findById.get();
        assertThat(get.getNumber()).isEqualTo(123456);
        assertThat(get.getName()).isEqualTo("itemGradeNew");
        assertThat(get.getUseAt()).isTrue();
    }

    @Test
    void changeUseAt() {
        ItemGrade itemGrade = new ItemGrade(
                12345,
                "itemGrade"
        );
        itemGradeRepository.save(itemGrade);

        itemGrade.changeUseAt(false);
        Optional<ItemGrade> findById = itemGradeRepository.findById(itemGrade.getId());

        assertThat(findById.isPresent()).isTrue();
        ItemGrade get = findById.get();
        assertThat(get.getUseAt()).isFalse();
    }
}