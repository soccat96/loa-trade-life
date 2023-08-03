package com.example.loatradelife.service;

import com.example.loatradelife.domain.Event;
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
class EventServiceTest {
    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void saveEvent() {
        Event event1 = new Event(
                "title1",
                "thumbnail1",
                "link1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        Event event2 = new Event(
                "title2",
                "thumbnail2",
                "link2",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Long id1 = eventService.saveEvent(event1);
        Long id2 = eventService.saveEvent(event2);
        List<Event> eventList = eventService.findAllEvents();

        assertThat(id1).isNotEqualTo(-1);
        assertThat(id2).isEqualTo(id1 + 1);
        assertThat(eventList.size()).isEqualTo(2);
    }

    @Test
    public void duplicatedSaveEvent() {
        Event event = new Event(
                "title_duplicate",
                "thumbnail",
                "link",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Long id1 = eventService.saveEvent(event);
        Long id2 = eventService.saveEvent(event);

        assertThat(id1).isEqualTo(1);
        assertThat(id2).isEqualTo(-1);
    }

    @Test
    public void updateEvent() {
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event(
                "title",
                "thumbnail",
                "link",
                now,
                now,
                now
        );
        Event updateEvent = new Event(
                "title_new",
                "thumbnail_new",
                "link_new",
                now.plusDays(1),
                now.plusDays(1),
                now.plusDays(1)
        );

        Long id = eventService.saveEvent(event);
        eventService.updateEvent(id, updateEvent);
        Optional<Event> oneEvent = eventService.findOneEvent(id);

        assertThat(oneEvent.isPresent()).isTrue();
        Event getEvent = oneEvent.get();
        assertThat(getEvent.getId()).isEqualTo(id);
        assertThat(getEvent.getTitle()).isEqualTo("title_new");
        assertThat(getEvent.getThumbnail()).isEqualTo("thumbnail_new");
        assertThat(getEvent.getLink()).isEqualTo("link_new");
        assertThat(getEvent.getStartDate().isEqual(now.plusDays(1))).isTrue();
        assertThat(getEvent.getEndDate().isEqual(now.plusDays(1))).isTrue();
        assertThat(getEvent.getRewardDate().isEqual(now.plusDays(1))).isTrue();
    }

    @Test
    public void deleteEvent() {
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event(
                "title",
                "thumbnail",
                "link",
                now,
                now,
                now
        );

        Long id = eventService.saveEvent(event);
        eventService.deleteEvent(id);

        Optional<Event> oneEvent = eventService.findOneEvent(id);
        assertThat(oneEvent.isEmpty()).isTrue();
    }
}