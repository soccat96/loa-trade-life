package com.example.loatradelife.repository;

import com.example.loatradelife.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsEventByTitle(String title);
}
