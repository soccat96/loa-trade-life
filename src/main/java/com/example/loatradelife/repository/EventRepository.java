package com.example.loatradelife.repository;

import com.example.loatradelife.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsEventByTitle(String title);

    List<Event> findByStartDateBetweenOrderByStartDate(LocalDateTime startDate, LocalDateTime endDate);
}
