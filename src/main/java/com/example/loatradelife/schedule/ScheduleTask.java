package com.example.loatradelife.schedule;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import com.example.loatradelife.domain.*;
import com.example.loatradelife.repository.ItemGradeRepository;
import com.example.loatradelife.repository.MarketItemRepository;
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
    private final ItemGradeRepository itemGradeRepository;
    private final MarketItemRepository marketItemRepository;
    private final MarketItemService marketItemService;
    private final MarketItemTradeInfoDailyService marketItemTradeInfoDailyService;

    private boolean init = false;

    private void initData() {
        init = true;

        ItemGrade normal = new ItemGrade(0, "일반", true);
        ItemGrade uncommon = new ItemGrade(1, "고급", true);
        ItemGrade rare = new ItemGrade(2, "희귀", true);
        ItemGrade epic = new ItemGrade(3, "영웅", true);
        itemGradeRepository.saveAllAndFlush(List.of(
                normal,
                uncommon,
                rare,
                epic,
                new ItemGrade(4, "전설", true),
                new ItemGrade(5, "유물", true),
                new ItemGrade(6, "고대", true),
                new ItemGrade(7, "에스더", true)
        ));
        marketItemRepository.saveAllAndFlush(List.of(
                new MarketItem(90200, 6882101, "들꽃", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_46.png", true),
                new MarketItem(90200, 6882104, "수줍은 들꽃", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_4_14.png", true),
                new MarketItem(90200, 6882107, "화사한 들꽃", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_47.png", true),

                new MarketItem(90200, 6882201, "투박한 버섯", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_56.png", true),
                new MarketItem(90200, 6882204, "싱싱한 버섯", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/all_quest/all_quest_02_101.png", true),
                new MarketItem(90200, 6882207, "화려한 버섯", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_57.png", true),

                new MarketItem(90300, 6882301, "목재", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_3_252.png", true),
                new MarketItem(90300, 6882304, "부드러운 목재", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_3_253.png", true),
                new MarketItem(90300, 6884307, "튼튼한 목재", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_4_4.png", true),

                new MarketItem(90400, 6882401, "철광석", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_3_243.png", true),
                new MarketItem(90400, 6882404, "묵직한 철광석", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_3_239.png", true),
                new MarketItem(90400, 6884407, "단단한 철광석", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_5_76.png", true),

                new MarketItem(90500, 6882501, "두툼한 생고기", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_2_192.png", true),
                new MarketItem(90500, 6882504, "다듬은 생고기", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_2_196.png", true),
                new MarketItem(90500, 6885508, "오레하 두툼한 생고기", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_67.png", true),
                new MarketItem(90500, 6882505, "질긴 가죽", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_2_204.png", true),
                new MarketItem(90500, 6882508, "수렵의 결정", 3, epic, "https://cdn-lostark.game.onstove.com/efui_iconatlas/all_quest/all_quest_02_65.png", true),

                new MarketItem(90600, 6882601, "생선", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_1_142.png", true),
                new MarketItem(90600, 6882604, "붉은 살 생선", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_4_49.png", true),
                new MarketItem(90600, 6885608, "오레하 태양 잉어", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_74.png", true),
                new MarketItem(90600, 6882605, "자연산 진주", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_8_72.png", true),
                new MarketItem(90600, 6882608, "낚시의 결정", 3, epic, "https://cdn-lostark.game.onstove.com/efui_iconatlas/all_quest/all_quest_04_183.png", true),

                new MarketItem(90700, 6882701, "고대 유물", 3, normal, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_9_3.png", true),
                new MarketItem(90700, 6882704, "희귀한 유물", 3, uncommon, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_9_4.png", true),
                new MarketItem(90700, 6885708, "오레하 유물", 3, rare, "https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_9_11.png", true),
                new MarketItem(90700, 6882708, "고고학의 결정", 3, epic, "https://cdn-lostark.game.onstove.com/efui_iconatlas/all_quest/all_quest_02_67.png", true)
        ));
    }

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

        if (!init) {
            initData();
        }

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
                            .bundleCount((Integer) x.get("BundleCount"))
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
