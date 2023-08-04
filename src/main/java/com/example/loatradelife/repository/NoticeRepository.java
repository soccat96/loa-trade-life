package com.example.loatradelife.repository;

import com.example.loatradelife.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    boolean existsNoticeByTitle(String title);
}
