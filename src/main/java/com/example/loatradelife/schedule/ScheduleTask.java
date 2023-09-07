package com.example.loatradelife.schedule;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import com.example.loatradelife.domain.*;
import com.example.loatradelife.service.EventService;
import com.example.loatradelife.service.MarketItemService;
import com.example.loatradelife.service.MarketItemTradeInfoDailyService;
import com.example.loatradelife.service.NoticeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleTask {
    private final LostArkOpenApiConfig lostArkOpenApiConfig;
    private final EventService eventService;
    private final NoticeService noticeService;
    private final MarketItemService marketItemService;
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    private void loggingStart(String methodName) {
        log.info("start " + methodName + "::==" + LocalDateTime.now());
    }

    private void loggingEnd(String methodName, int addedEventCount) {
        log.info("added event count::==" + addedEventCount);
        log.info("end " + methodName + "::==" + LocalDateTime.now());
    }

    private HttpURLConnection getHttpURLConnection(String url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(lostArkOpenApiConfig.getBaseUrl() + url).openConnection();
            connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
            // connection exception
        }
    }

    private List<Map<String, Object>> getMapList(HttpURLConnection connection) {
        ObjectMapper ob = new ObjectMapper();
        try {
            return ob.readValue(connection.getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
            // data transfer exception
        }
    }

    // second minute hour date month dayOfWeek
    @Scheduled(cron = "0 */1 * * * *")
    public void getEvents() {
        loggingStart("getEvents()");

        int addedEventCount = 0;
        List<Map<String, Object>> mapList = getMapList(getHttpURLConnection("/news/events"));
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

            Long result = eventService.saveEvent(event);
            if (result > 0) {
                addedEventCount++;
            }
        }

        loggingEnd("getEvents()", addedEventCount);
    }

    // second minute hour date month dayOfWeek
    @Scheduled(cron = "0 */1 * * * *")
    public void getNotices() {
        loggingStart("getNotices()");

        int addedNoticeCount = 0;
        List<Map<String, Object>> mapList = getMapList(getHttpURLConnection("/news/notices"));
        for (Map<String, Object> x : mapList) {
            Notice notice = new Notice(
                    (String) x.get("Title"),
                    LocalDateTime.parse((String) x.get("Date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    (String) x.get("Link"),
                    NoticeType.getNoticeType((String) x.get("Type"))
            );

            Long result = noticeService.saveNotice(notice);
            if (result > 0) {
                addedNoticeCount++;
            }
        }

        loggingEnd("getNotices()", addedNoticeCount);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void getMarketItemTradeInfoDailies() {
        loggingStart("getMarketItemTradeInfoDailies()");

        int addedCount = 0;
        List<MarketItem> marketItemList = marketItemService.findAllMarketItem();
        for (MarketItem marketItem : marketItemList) {
            List<Map<String, Object>> mapList = getMapList(getHttpURLConnection("/markets/items/" + marketItem.getCode()));
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

                Long result = marketItemTradeInfoDailyService.saveMarketItemTradeInfoDaily(data);
                if (result > 0) {
                    addedCount++;
                }
            }
        }

        loggingEnd("getMarketItemTradeInfoDailies()", addedCount);
    }
}