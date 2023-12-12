package com.example.loatradelife.schedule;

import com.example.loatradelife.connection.ExternalLinkCreator;
import com.example.loatradelife.domain.*;
import com.example.loatradelife.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleTask {
    private final ExternalLinkCreator externalLinkCreator;
    private final EventService eventService;
    private final NoticeService noticeService;
    private final InspectionTimeService inspectionTimeService;
    private final MarketItemService marketItemService;
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    private boolean checkInspectionTime() {
        LocalDateTime now = LocalDateTime.now();
        Optional<InspectionTime> inspectionTime = inspectionTimeService.selectOneRecentInspectionTime();
        return inspectionTime.map(time -> time.isBetween(now)).orElse(false);
    }

    private List<Map<String, Object>> getMapList(HttpURLConnection connection) {
        try {
            return new ObjectMapper().readValue(connection.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            log.error("check: response json format", e);
            throw new RuntimeException();
        }
    }

    // second minute hour date month dayOfWeek
    @SuppressWarnings("unchecked")
    @Scheduled(cron = "0 */5 * * * *")
    public void getMarketItemTradeInfoDailies() {
        if (checkInspectionTime()) {
            return ;
        }

        log.info("start getMarketItemTradeInfoDailies()");

        List<MarketItem> marketItemList = marketItemService.findAllMarketItem();
        for (MarketItem marketItem : marketItemList) {
            List<Map<String, Object>> mapList = getMapList(externalLinkCreator.getApiHttpURLConnection("/markets/items/" + marketItem.getCode()));
            Map<String, Object> obj = mapList.get(0);
            Integer bundleCount = (Integer) obj.get("BundleCount");
            List<Map<String, Object>> dataList = (List<Map<String,Object>>) obj.get("Stats");
            for (Map<String, Object> x : dataList) {
                MarketItemTradeInfoDaily data = MarketItemTradeInfoDaily.builder()
                        .marketItem(marketItem)
                        .date(
                                LocalDateTime.of(
                                        LocalDate.parse((String) x.get("Date"), DateTimeFormatter.ISO_LOCAL_DATE),
                                        LocalTime.of(0, 0, 0)
                                )
                        )
                        .avgPrice((Double) x.get("AvgPrice"))
                        .tradeCount((Integer) x.get("TradeCount"))
                        .bundleCount(bundleCount)
                        .build();

                marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data);
            }
        }

        log.info("end getMarketItemTradeInfoDailies()");
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void getEvents() {
        if (checkInspectionTime()) {
            return ;
        }

        log.info("start getEvents()");

        List<Map<String, Object>> mapList = getMapList(externalLinkCreator.getApiHttpURLConnection("/news/events"));
        for (Map<String, Object> m : mapList) {
            Event event = new Event(
                    (String) m.get("Title"),
                    (String) m.get("Thumbnail"),
                    (String) m.get("Link"),
                    LocalDateTime.parse((String )m.get("StartDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    LocalDateTime.parse((String) m.get("EndDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    m.get("RewardDate") == null
                            ? null
                            : LocalDateTime.parse((String) m.get("RewardDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

            eventService.saveEvent(event);
        }

        log.info("end getEvents()");
    }

    // second minute hour date month dayOfWeek
    @Scheduled(cron = "0 */10 * * * *")
    public void getNotices() {
        if (checkInspectionTime()) {
            return ;
        }

        log.info("start getNotices()");

        DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hm = DateTimeFormatter.ofPattern("HH:mm");
        String cssQuery = "section.article__data div.fr-view p span[style]";

        List<Map<String, Object>> mapList = getMapList(externalLinkCreator.getApiHttpURLConnection("/news/notices"));
        for (Map<String, Object> x : mapList) {
            Notice notice = new Notice(
                    (String) x.get("Title"),
                    LocalDateTime.parse((String) x.get("Date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    (String) x.get("Link"),
                    NoticeType.getNoticeType((String) x.get("Type"))
            );

            Long result = noticeService.saveNotice(notice);
            if (result > 0) {
                if (notice.getTitle().contains("점검 안내")) {
                    Document document = externalLinkCreator.getJsopDocument(notice.getLink());
                    String text = document.select(cssQuery).get(1).text();
                    String[] split = text.trim().split(" ~ ");

                    try {
                        inspectionTimeService.createInspectionTime(new InspectionTime(
                                notice,
                                LocalDateTime.of(
                                        LocalDate.parse(split[0].substring(0, 10), ymd),
                                        LocalTime.parse(split[0].substring(14, 19), hm)
                                ),
                                LocalDateTime.of(
                                        LocalDate.parse(split[1].substring(0, 10), ymd),
                                        LocalTime.parse(split[1].substring(14, 19), hm)
                                )
                        ));
                    } catch (DateTimeParseException e) {
                        // TODO: 2023/12/06 How to terminate normally other than an exception in @Scheduled...
                        log.error("check: date time string format, [" + text + "]", e);
                        throw new RuntimeException();
                    }
                }
            }
        }

        log.info("end getNotices()");
    }
}