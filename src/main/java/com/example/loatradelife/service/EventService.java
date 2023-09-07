package com.example.loatradelife.service;

import com.example.loatradelife.domain.Event;
import com.example.loatradelife.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public Long saveEvent(Event event) {
        long id = -1;

        if (!eventRepository.existsEventByTitle(event.getTitle())) {
            id = eventRepository.save(event).getId();
        }

        return id;
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> findOneEvent(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> findListByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByStartDateBetweenOrderByStartDate(startDate, endDate);
    }

    @Transactional
    public void updateEvent(Long id, Event event) {
        eventRepository.findById(id).ifPresent(value -> value.updateEvent(event));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
