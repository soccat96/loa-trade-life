package com.example.loatradelife.schedule;

import com.example.loatradelife.config.LostArkOpenApiConfig;
import com.example.loatradelife.domain.Event;
import com.example.loatradelife.service.EventService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleTask {
    private final LostArkOpenApiConfig lostArkOpenApiConfig;
    private final EventService eventService;

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
}
