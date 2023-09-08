package com.example.loatradelife.controller;

import com.example.loatradelife.domain.Event;
import com.example.loatradelife.service.EventService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Result<List<EventDto>> getEvents(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDateTime sd = LocalDateTime.of(
                LocalDate.now().minusDays(7),
                LocalTime.of(0, 0, 0)
        );
        LocalDateTime ed = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(23, 59, 59)
        );
        if (startDate != null && !startDate.isEmpty()) {
            sd = LocalDateTime.of(
                    LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(0, 0, 0)
            );
            ed = LocalDateTime.of(
                    LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(23, 59, 59)
            );
        }

        ArrayList<EventDto> eventDtoList = new ArrayList<>();
        List<Event> eventList = eventService.findListByStartDateBetween(sd, ed);
        for (Event x : eventList) {
            eventDtoList.add(new EventDto(x));
        }

        return new Result<>(eventDtoList);
    }

    @GetMapping("/{eventId}")
    public Result<EventDto> getEvent(@PathVariable(name = "eventId") String eventId) {
        Optional<Event> oneEvent = eventService.findOneEvent(Long.parseLong(eventId));
        return oneEvent.map(event -> new Result<>(new EventDto(event))).orElseGet(() -> new Result<>(new EventDto()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class EventDto {
        private Long id;
        private String title;
        private String thumbnail;
        private String link;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime rewardDate;

        public EventDto(Event event) {
            this.id = event.getId();
            this.title = event.getTitle();
            this.thumbnail = event.getThumbnail();
            this.link = event.getLink();
            this.startDate = event.getStartDate();
            this.endDate = event.getEndDate();
            this.rewardDate = event.getRewardDate();
        }
    }
}
