package com.example.loatradelife.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
public class InspectionTimeTest {
    @Test
    public void updateInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Notice notice = new Notice("title", now, "link", NoticeType.INSPECTION);
        InspectionTime inspectionTime = new InspectionTime(notice, now.plusHours(1), now.plusHours(2));

        inspectionTime.updateTimes(now.plusHours(3), now.plusHours(4));

        assertThat(inspectionTime.getStartTime()).isEqualTo(now.plusHours(3));
        assertThat(inspectionTime.getEndTime()).isEqualTo(now.plusHours(4));
    }
}