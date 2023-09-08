package com.example.loatradelife.repository;

import com.example.loatradelife.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    boolean existsNoticeByTitle(String title);

    List<Notice> findByDateBetweenOrderByDate(LocalDateTime startDate, LocalDateTime endDate);
}
