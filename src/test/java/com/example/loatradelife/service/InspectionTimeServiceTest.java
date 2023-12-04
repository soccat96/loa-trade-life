package com.example.loatradelife.service;

import com.example.loatradelife.domain.InspectionTime;
import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.domain.NoticeType;
import com.example.loatradelife.repository.InspectionTimeRepository;
import com.example.loatradelife.repository.NoticeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class InspectionTimeServiceTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private InspectionTimeService inspectionTimeService;
    @Autowired
    private InspectionTimeRepository inspectionTimeRepository;

    @BeforeEach
    public void setUp() {
        inspectionTimeRepository.deleteAll();
        noticeRepository.deleteAll();
    }

    @Test
    public void createInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice("title", now, "link", NoticeType.INSPECTION);
        InspectionTime inspectionTime = new InspectionTime(notice, now.plusHours(1), now.plusHours(2));

        inspectionTimeService.createInspectionTime(inspectionTime);

        assertThat(inspectionTime.getId()).isGreaterThan(0);
    }

    @Test
    public void selectAllInspectionTIme() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice0 = new Notice("title0", now, "link", NoticeType.INSPECTION);
        Notice notice1 = new Notice("title1", now, "link", NoticeType.INSPECTION);
        Notice notice2 = new Notice("title2", now, "link", NoticeType.INSPECTION);

        InspectionTime inspectionTime0 = new InspectionTime(notice0, now.plusHours(1), now.plusHours(2));
        InspectionTime inspectionTime1 = new InspectionTime(notice1, now.plusHours(2), now.plusHours(3));
        InspectionTime inspectionTime2 = new InspectionTime(notice2, now.plusHours(3), now.plusHours(4));

        noticeService.saveNotice(notice0);
        noticeService.saveNotice(notice1);
        noticeService.saveNotice(notice2);
        inspectionTimeService.createInspectionTime(inspectionTime0);
        inspectionTimeService.createInspectionTime(inspectionTime1);
        inspectionTimeService.createInspectionTime(inspectionTime2);

        List<InspectionTime> inspectionTimeList = inspectionTimeService.selectAllInspectionTime();
        assertThat(inspectionTimeList.size()).isEqualTo(3);
    }

    @Test
    public void selectOneInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice0 = new Notice("title0", now, "link", NoticeType.INSPECTION);
        Notice notice1 = new Notice("title1", now, "link", NoticeType.INSPECTION);
        Notice notice2 = new Notice("title2", now, "link", NoticeType.INSPECTION);

        InspectionTime inspectionTime0 = new InspectionTime(notice0, now.plusHours(1), now.plusHours(2));
        InspectionTime inspectionTime1 = new InspectionTime(notice1, now.plusHours(2), now.plusHours(3));
        InspectionTime inspectionTime2 = new InspectionTime(notice2, now.plusHours(3), now.plusHours(4));

        noticeService.saveNotice(notice0);
        noticeService.saveNotice(notice1);
        noticeService.saveNotice(notice2);
        inspectionTimeService.createInspectionTime(inspectionTime0);
        inspectionTimeService.createInspectionTime(inspectionTime1);
        inspectionTimeService.createInspectionTime(inspectionTime2);

        Optional<InspectionTime> findOne = inspectionTimeService.selectOneInspectionTime(inspectionTime1.getId());
        assertThat(findOne.isPresent()).isTrue();
        InspectionTime it = findOne.get();
        assertThat(it.getId()).isEqualTo(inspectionTime1.getId());
        assertThat(it.getNotice().getId()).isEqualTo(notice1.getId());
        assertThat(it.getStartTime()).isEqualTo(now.plusHours(2));
        assertThat(it.getEndTime()).isEqualTo(now.plusHours(3));
    }

    @Test
    public void selectOneRecentInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice0 = new Notice("title0", now, "link", NoticeType.INSPECTION);
        Notice notice1 = new Notice("title1", now, "link", NoticeType.INSPECTION);
        Notice notice2 = new Notice("title2", now, "link", NoticeType.INSPECTION);

        InspectionTime inspectionTime0 = new InspectionTime(notice0, now.plusHours(1), now.plusHours(2));
        InspectionTime inspectionTime1 = new InspectionTime(notice1, now.plusHours(2), now.plusHours(3));
        InspectionTime inspectionTime2 = new InspectionTime(notice2, now.plusHours(3), now.plusHours(4));

        noticeService.saveNotice(notice0);
        noticeService.saveNotice(notice1);
        noticeService.saveNotice(notice2);
        inspectionTimeService.createInspectionTime(inspectionTime0);
        inspectionTimeService.createInspectionTime(inspectionTime1);
        inspectionTimeService.createInspectionTime(inspectionTime2);

        Optional<InspectionTime> inspectionTime = inspectionTimeService.selectOneRecentInspectionTime();
        assertThat(inspectionTime.isPresent()).isTrue();
        InspectionTime it = inspectionTime.get();
        assertThat(it.getId()).isEqualTo(inspectionTime2.getId());
    }

    @Test
    public void updateInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice("title", now, "link", NoticeType.INSPECTION);
        noticeService.saveNotice(notice);
        InspectionTime inspectionTime = new InspectionTime(notice, now.plusHours(1), now.plusHours(2));
        long id = inspectionTimeService.createInspectionTime(inspectionTime);

        Optional<InspectionTime> findOne = inspectionTimeService.selectOneInspectionTime(id);
        assertThat(findOne.isPresent()).isTrue();
        InspectionTime it = findOne.get();
        it.updateTimes(now.plusHours(2), now.plusHours(3));
        entityManager.persist(it);
        entityManager.flush();
        Optional<InspectionTime> findOneUpdated = inspectionTimeService.selectOneInspectionTime(id);
        assertThat(findOneUpdated.isPresent()).isTrue();
        InspectionTime itUpdated = findOneUpdated.get();
        assertThat(itUpdated.getStartTime()).isEqualTo(now.plusHours(2));
        assertThat(itUpdated.getEndTime()).isEqualTo(now.plusHours(3));
    }

    @Test
    public void deleteInspectionTIme() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice("title", now, "link", NoticeType.INSPECTION);
        noticeService.saveNotice(notice);
        InspectionTime inspectionTime = new InspectionTime(notice, now.plusHours(1), now.plusHours(2));
        long id = inspectionTimeService.createInspectionTime(inspectionTime);

        Optional<InspectionTime> findOne = inspectionTimeService.selectOneInspectionTime(id);
        assertThat(findOne.isPresent()).isTrue();
        inspectionTimeService.deleteInspectionTime(id);
        Optional<InspectionTime> findOneAfterDelete = inspectionTimeService.selectOneInspectionTime(id);
        assertThat(findOneAfterDelete.isPresent()).isFalse();
    }
}