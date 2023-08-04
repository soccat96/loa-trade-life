package com.example.loatradelife.service;

import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.domain.NoticeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
@Transactional
class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void saveNotice() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice1 = new Notice(
                "title1",
                now,
                "link1",
                NoticeType.NOTICE
        );
        Notice notice2 = new Notice(
                "title2",
                now.plusMinutes(1),
                "link2",
                NoticeType.NOTICE
        );

        Long id1 = noticeService.saveNotice(notice1);
        Long id2 = noticeService.saveNotice(notice2);

        List<Notice> noticeList = noticeService.findAllNotice();
        assertThat(id1).isNotEqualTo(-1);
        assertThat(id2).isEqualTo(id1 + 1);
        assertThat(noticeList.size()).isEqualTo(2);
    }

    @Test
    public void duplicateSaveNotice() {
        Notice notice = new Notice(
                "title",
                LocalDateTime.now(),
                "link",
                NoticeType.NOTICE
        );

        Long id1 = noticeService.saveNotice(notice);
        Long id2 = noticeService.saveNotice(notice);

        List<Notice> noticeList = noticeService.findAllNotice();
        assertThat(id1).isNotEqualTo(-1);
        assertThat(id2).isEqualTo(-1);
        assertThat(noticeList.size()).isEqualTo(1);
    }

    @Test
    public void updateNotice() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice(
                "title",
                now,
                "link",
                NoticeType.NOTICE
        );
        Notice newNotice = new Notice(
                "title_new",
                now.plusMinutes(1),
                "link_new",
                NoticeType.INSPECTION
        );

        Long id = noticeService.saveNotice(notice);
        noticeService.updateNotice(id, newNotice);
        Optional<Notice> oneNotice = noticeService.findOneNotice(id);

        assertThat(oneNotice.isPresent()).isTrue();
        Notice getNotice = oneNotice.get();
        assertThat(getNotice.getId()).isEqualTo(id);
        assertThat(getNotice.getTitle()).isEqualTo(newNotice.getTitle());
        assertThat(getNotice.getDate().isEqual(newNotice.getDate())).isTrue();
        assertThat(getNotice.getLink()).isEqualTo(newNotice.getLink());
        assertThat(getNotice.getType()).isEqualTo(newNotice.getType());
    }

    @Test
    public void deleteNotice() {
        Notice notice = new Notice(
                "title",
                LocalDateTime.now(),
                "link",
                NoticeType.NOTICE
        );

        Long id = noticeService.saveNotice(notice);
        noticeService.deleteNotice(id);

        Optional<Notice> oneNotice = noticeService.findOneNotice(id);
        assertThat(oneNotice.isEmpty()).isTrue();
    }
}