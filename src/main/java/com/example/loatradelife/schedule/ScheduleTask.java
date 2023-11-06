package com.example.loatradelife.schedule;

import com.example.loatradelife.connection.ConnectionUtil;
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
    private final ConnectionUtil connectionUtil;
    private final EventService eventService;
    private final NoticeService noticeService;
    private final MarketItemService marketItemService;
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    private void loggingStart(String methodName) {
        log.info("start " + methodName + "::==" + LocalDateTime.now());
    }

    private void loggingEnd(String methodName, int addedEventCount) {
        log.info("added count::==" + addedEventCount);
        log.info("end " + methodName + "::==" + LocalDateTime.now());
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
    @Scheduled(cron = "0 */5 * * * *")
    public void getMarketItemTradeInfoDailies() {
        loggingStart("getMarketItemTradeInfoDailies()");

        int addedCount = 0;
        List<MarketItem> marketItemList = marketItemService.findAllMarketItem();
        for (MarketItem marketItem : marketItemList) {
            List<Map<String, Object>> mapList = getMapList(connectionUtil.getApiHttpURLConnection("/markets/items/" + marketItem.getCode()));
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

    @Scheduled(cron = "0 0,30 * * * *")
    public void getEvents() {
        loggingStart("getEvents()");

        int addedEventCount = 0;
        List<Map<String, Object>> mapList = getMapList(connectionUtil.getApiHttpURLConnection("/news/events"));
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
    @Scheduled(cron = "0 */10 * * * *")
    public void getNotices() {
        loggingStart("getNotices()");

        int addedNoticeCount = 0;
        List<Map<String, Object>> mapList = getMapList(connectionUtil.getApiHttpURLConnection("/news/notices"));
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
}