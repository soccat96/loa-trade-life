package com.example.loatradelife.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class EventTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void updateEvent() {
        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        Event event = new Event(
                "event title",
                "event thumbnail",
                "event link",
                now,
                now.plusMonths(1),
                now.plusMonths(1).plusWeeks(1)
        );
        LocalDateTime newNow = now.plusDays(1);
        Event newEvent = new Event(
                "new title",
                "new thumbnail",
                "new link",
                newNow,
                newNow.plusMonths(1),
                newNow.plusMonths(1).plusWeeks(1)
        );

        event.update(newEvent);

        assertThat(event.getId()).isNull();
        assertThat(event.getTitle()).isEqualTo(newEvent.getTitle());
        assertThat(event.getThumbnail()).isEqualTo(newEvent.getThumbnail());
        assertThat(event.getLink()).isEqualTo(newEvent.getLink());
        assertThat(event.getStartDate()).isEqualTo(newEvent.getStartDate());
        assertThat(event.getEndDate()).isEqualTo(newEvent.getEndDate());
        assertThat(event.getRewardDate()).isEqualTo(newEvent.getRewardDate());
    }
}