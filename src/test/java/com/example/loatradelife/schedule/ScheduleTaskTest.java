package com.example.loatradelife.schedule;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ScheduleTaskTest {
    @Test
    public void getInspection() throws IOException {
        String inspection = "https://lostark.game.onstove.com/News/Notice/Views/2563";

        Document document = Jsoup.connect(inspection).get();

        // then
    }

    @Test
    public void inspectionParse() throws IOException {
        // DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd(E) HH:mm");
        DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        String complexInspection = "https://lostark.game.onstove.com/News/Notice/Views/2563"; // 2023-10-11(수) 06:00 ~ 2023-10-11(수) 10:00 (4시간)
        String simpleInspection = "https://lostark.game.onstove.com/News/Notice/Views/2573"; // 2023-10-18(수) 03:00 ~ 2023-10-18(수) 10:00 (7시간)

        String complexTimeText = Jsoup.connect(complexInspection).get()
                .select("section.article__data div.fr-view p span[style]").get(1).text();
        String[] complexSplit = complexTimeText.trim().split(" ~ ");
        LocalDate complexDateStart = LocalDate.parse(complexSplit[0].substring(0, 10), ymd);
        LocalTime complexTimeStart = LocalTime.parse(complexSplit[0].substring(14, 19), hm);
        LocalDate complexDateEnd = LocalDate.parse(complexSplit[1].substring(0, 10), ymd);
        LocalTime complexTimeEnd = LocalTime.parse(complexSplit[1].substring(14, 19), hm);
        String simpleTimeText = Jsoup.connect(simpleInspection).get()
                .select("section.article__data div.fr-view p span[style]").get(1).text();
        String[] simpleSplit = simpleTimeText.split(" ~ ");
        LocalDate simpleDateStart = LocalDate.parse(simpleSplit[0].substring(0, 10), ymd);
        LocalTime simpleTimeStart = LocalTime.parse(simpleSplit[0].substring(14, 19), hm);
        LocalDate simpleDateEnd = LocalDate.parse(simpleSplit[1].substring(0, 10), ymd);
        LocalTime simpleTimeEnd = LocalTime.parse(simpleSplit[1].substring(14, 19), hm);

        // then
        assertThat(LocalDateTime.of(complexDateStart, complexTimeStart)).isEqualTo(LocalDateTime.of(2023, 10, 11, 6, 0, 0, 0));
        assertThat(LocalDateTime.of(complexDateEnd, complexTimeEnd)).isEqualTo(LocalDateTime.of(2023, 10, 11, 10, 0, 0, 0));
        assertThat(LocalDateTime.of(simpleDateStart, simpleTimeStart)).isEqualTo(LocalDateTime.of(2023, 10, 18, 3, 0, 0, 0));
        assertThat(LocalDateTime.of(simpleDateEnd, simpleTimeEnd)).isEqualTo(LocalDateTime.of(2023, 10, 18, 10, 0, 0, 0));
    }
}