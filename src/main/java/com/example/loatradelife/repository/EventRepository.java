package com.example.loatradelife.repository;

import com.example.loatradelife.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsEventByTitleAndStartDate(String title, LocalDateTime startDate);
    List<Event> findByStartDateBetweenOrderByStartDate(LocalDateTime startDate, LocalDateTime endDate);
}
