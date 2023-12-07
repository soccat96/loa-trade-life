package com.example.loatradelife.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoticeTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateNotice() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice(
                "notice",
                now,
                "link",
                NoticeType.NOTICE
        );
        Notice newNotice = new Notice(
                "new notice",
                now.plusDays(1),
                "new link",
                NoticeType.INSPECTION
        );

        notice.update(newNotice);

        assertThat(notice.getId()).isNull();
        assertThat(notice.getTitle()).isEqualTo("new notice");
        assertThat(notice.getDate().isEqual(now.plusDays(1))).isTrue();
        assertThat(notice.getLink()).isEqualTo("new link");
        assertThat(notice.getType()).isEqualTo(NoticeType.INSPECTION);
    }
}