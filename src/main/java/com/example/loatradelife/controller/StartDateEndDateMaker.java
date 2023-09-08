package com.example.loatradelife.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class StartDateEndDateMaker {
    public static Map<String, LocalDateTime> getStartDateEndDate(String startDate, String endDate) {
        Map<String, LocalDateTime> map = getDefault();

        if (startDate != null && !startDate.isEmpty()) {
            map.put("sd", LocalDateTime.of(
                    LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(0, 0, 0)
            ));
            map.put("ed", LocalDateTime.of(
                    LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(23, 59, 59)
            ));
        }

        return map;
    }

    private static Map<String, LocalDateTime> getDefault() {
        HashMap<String, LocalDateTime> map = new HashMap<>();

        LocalDateTime sd = LocalDateTime.of(
                LocalDate.now().minusDays(7),
                LocalTime.of(0, 0, 0)
        );
        LocalDateTime ed = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(23, 59, 59)
        );
        map.put("sd", sd);
        map.put("ed", ed);

        return map;
    }
}
