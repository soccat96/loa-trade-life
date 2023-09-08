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

    @Test
    public void findByDateBetween() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ed = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime sd = ed.minusDays(10);
        Notice notice00 = new Notice("title00", sd.minusDays(1), "link0", NoticeType.NOTICE);
        Notice notice01 = new Notice("title01", sd.minusDays(0), "link0", NoticeType.NOTICE);
        Notice notice02 = new Notice("title02", sd.plusDays(1), "link0", NoticeType.NOTICE);
        Notice notice03 = new Notice("title03", sd.plusDays(2), "link0", NoticeType.NOTICE);
        Notice notice04 = new Notice("title04", sd.plusDays(3), "link0", NoticeType.NOTICE);
        Notice notice05 = new Notice("title05", sd.plusDays(4), "link0", NoticeType.NOTICE);
        Notice notice06 = new Notice("title06", sd.plusDays(5), "link0", NoticeType.NOTICE);
        Notice notice07 = new Notice("title07", sd.plusDays(6), "link0", NoticeType.NOTICE);
        Notice notice08 = new Notice("title08", sd.plusDays(7), "link0", NoticeType.NOTICE);
        Notice notice09 = new Notice("title09", sd.plusDays(8), "link0", NoticeType.NOTICE);
        Notice notice10 = new Notice("title10", sd.plusDays(9), "link0", NoticeType.NOTICE);
        Notice notice11 = new Notice("title11", sd.plusDays(10), "link0", NoticeType.NOTICE);
        Notice notice12 = new Notice("title12", sd.plusDays(11), "link0", NoticeType.NOTICE);
        Notice notice13 = new Notice("title13", sd.plusDays(12), "link0", NoticeType.NOTICE);
        Notice notice14 = new Notice("title14", sd.plusDays(13), "link0", NoticeType.NOTICE);
        noticeService.saveNotice(notice00);
        noticeService.saveNotice(notice01);
        noticeService.saveNotice(notice02);
        noticeService.saveNotice(notice03);
        noticeService.saveNotice(notice04);
        noticeService.saveNotice(notice05);
        noticeService.saveNotice(notice06);
        noticeService.saveNotice(notice07);
        noticeService.saveNotice(notice08);
        noticeService.saveNotice(notice09);
        noticeService.saveNotice(notice10);
        noticeService.saveNotice(notice11);
        noticeService.saveNotice(notice12);
        noticeService.saveNotice(notice13);
        noticeService.saveNotice(notice14);

        List<Notice> noticeList = noticeService.findListByDateBetween(sd, ed);

        assertThat(noticeList.size()).isEqualTo(11);
        Notice noticeFirst = noticeList.get(0);
        Notice noticeLast = noticeList.get(noticeList.size() - 1);
        assertThat(noticeFirst.getTitle()).isEqualTo("title01");
        assertThat(noticeFirst.getDate().isEqual(sd)).isTrue();
        assertThat(noticeLast.getTitle()).isEqualTo("title11");
        assertThat(noticeLast.getDate().isEqual(ed)).isTrue();
    }
}