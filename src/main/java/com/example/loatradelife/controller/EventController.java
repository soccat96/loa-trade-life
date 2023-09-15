package com.example.loatradelife.controller;

import com.example.loatradelife.controller.dto.EventDto;
import com.example.loatradelife.domain.Event;
import com.example.loatradelife.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<String, LocalDateTime> mapSdEd = StartDateEndDateMaker.getStartDateEndDate(startDate, endDate);

        ArrayList<EventDto> eventDtoList = new ArrayList<>();
        List<Event> eventList = eventService.findListByStartDateBetween(mapSdEd.get("sd"), mapSdEd.get("ed"));
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
}
