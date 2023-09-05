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

    // second minute hour date month dayOfWeek
    @Scheduled(cron = "0 */1 * * * *")
    public void getEvents() {
        log.info("start 'getEvents()'::==" + LocalDateTime.now());
        int addedEventCount = 0;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(lostArkOpenApiConfig.getBaseUrl() + "/news/events").openConnection();
            connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            ObjectMapper ob = new ObjectMapper();
            List<Map<String, String>> mapList = ob.readValue(connection.getInputStream(), new TypeReference<>() {});

            for (Map<String, String> m : mapList) {
                Event event = new Event(
                        m.get("Title"),
                        m.get("Thumbnail"),
                        m.get("Link"),
                        LocalDateTime.parse(m.get("StartDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        LocalDateTime.parse(m.get("EndDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        m.get("RewardDate") == null
                                ? null
                                : LocalDateTime.parse(m.get("RewardDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );

                Long result = eventService.saveEvent(event);
                if (result > 0) {
                    addedEventCount++;
                }
            }

            log.info("added event count::==" + addedEventCount);
            log.info("end 'getEvents()'::==" + LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // second minute hour date month dayOfWeek
    @Scheduled(cron = "0 */1 * * * *")
    public void getNotice() {
        log.info("start 'getNotice()::==" + LocalDateTime.now());
        int addedNoticeCount = 0;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(lostArkOpenApiConfig.getBaseUrl() + "/news/notices").openConnection();
            connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();

            ObjectMapper ob = new ObjectMapper();
            List<Map<String, String>> mapList = ob.readValue(connection.getInputStream(), new TypeReference<>() {});
            for (Map<String, String> x : mapList) {
                Notice notice = new Notice(
                        x.get("Title"),
                        LocalDateTime.parse(x.get("Date"), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        x.get("Link"),
                        NoticeType.getNoticeType(x.get("Type"))
                );

                Long result = noticeService.saveNotice(notice);
                if (result > 0) {
                    addedNoticeCount++;
                }
            }

            log.info("added notice count::==" + addedNoticeCount);
            log.info("end 'getNotice()::==" + LocalDateTime.now());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void getMarketItemTradeInfoDaily() {
        log.info("start 'getMarketItemTradeInfoDaily()::==" + LocalDateTime.now());
        int addedCount = 0;

        List<MarketItem> marketItemList = marketItemService.findAllMarketItem();
        for (MarketItem marketItem : marketItemList) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(lostArkOpenApiConfig.getBaseUrl() + "/markets/items/" + marketItem.getCode()).openConnection();
                connection.setRequestProperty("authorization", "bearer " + lostArkOpenApiConfig.getKey());
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestMethod("GET");
                connection.connect();

                ObjectMapper ob = new ObjectMapper();
                List<Map<String, Object>> mapList = ob.readValue(connection.getInputStream(), new TypeReference<>() {});
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("added market item trade info daily count::==" + addedCount);
        log.info("end 'getMarketItemTradeInfoDaily()::==" + LocalDateTime.now());
    }
}
