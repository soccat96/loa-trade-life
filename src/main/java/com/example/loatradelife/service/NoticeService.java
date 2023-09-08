package com.example.loatradelife.service;

import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Long saveNotice(Notice notice) {
        long id = -1;

        if (!noticeRepository.existsNoticeByTitle(notice.getTitle())) {
            id = noticeRepository.save(notice).getId();
        }

        return id;
    }

    public List<Notice> findAllNotice() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> findOneNotice(Long id) {
        return noticeRepository.findById(id);
    }

    public List<Notice> findListByDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return noticeRepository.findByDateBetweenOrderByDate(startDate, endDate);
    }

    @Transactional
    public void updateNotice(Long id, Notice notice) {
        noticeRepository.findById(id).ifPresent(value -> value.updateNotice(notice));
    }

    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}
