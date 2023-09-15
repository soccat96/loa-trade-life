package com.example.loatradelife.service;

import com.example.loatradelife.domain.Event;
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
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event(
                "title_duplicate",
                "thumbnail",
                "link",
                now,
                now,
                now
        );
        Event event1 = new Event(
                "title_duplicate",
                "thumbnail",
                "link",
                now.plusDays(1),
                now.plusDays(1),
                now.plusDays(1)
        );

        Long id1 = eventService.saveEvent(event);
        Long id2 = eventService.saveEvent(event);
        Long id3 = eventService.saveEvent(event1);

        assertThat(id1).isNotEqualTo(-1);
        assertThat(id2).isEqualTo(-1);
        assertThat(id3).isEqualTo(id1 + 1);
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

    @Test
    public void findByStartDateBetween() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ed = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime sd = ed.minusDays(7);
        Event event1 = new Event("title1","thumbnail1","link1",sd.minusDays(1),ed,ed);
        Event event2 = new Event("title2","thumbnail2","link2",sd.plusDays(0), ed,ed);
        Event event3 = new Event("title3","thumbnail3","link3",sd.plusDays(1), ed,ed);
        Event event4 = new Event("title4","thumbnail4","link4",sd.plusDays(2), ed,ed);
        Event event5 = new Event("title5","thumbnail5","link5",sd.plusDays(3), ed,ed);
        Event event6 = new Event("title6","thumbnail6","link6",sd.plusDays(4), ed,ed);
        Event event7 = new Event("title7","thumbnail7","link7",sd.plusDays(5), ed,ed);
        Event event8 = new Event("title8","thumbnail8","link8",sd.plusDays(6), ed,ed);
        Event event9 = new Event("title9","thumbnail9","link7",sd.plusDays(7), ed,ed);
        eventService.saveEvent(event1);
        eventService.saveEvent(event2);
        eventService.saveEvent(event3);
        eventService.saveEvent(event4);
        eventService.saveEvent(event5);
        eventService.saveEvent(event6);
        eventService.saveEvent(event7);
        eventService.saveEvent(event8);
        eventService.saveEvent(event9);

        List<Event> list = eventService.findListByStartDateBetween(sd, ed);

        assertThat(list.size()).isEqualTo(8);
        Event eventFirst = list.get(0);
        Event eventLast = list.get(list.size() - 1);
        assertThat(eventFirst.getTitle()).isEqualTo("title2");
        assertThat(eventLast.getTitle()).isEqualTo("title9");
    }
}