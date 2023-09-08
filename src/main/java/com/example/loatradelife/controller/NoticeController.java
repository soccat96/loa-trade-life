package com.example.loatradelife.controller;

import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.domain.NoticeType;
import com.example.loatradelife.service.NoticeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public Result<List<NoticeDto>> getNotices(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDateTime sd = LocalDateTime.of(
                LocalDate.now().minusDays(7),
                LocalTime.of(0, 0, 0)
        );
        LocalDateTime ed = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(23, 59, 59)
        );
        if (startDate != null && !startDate.isEmpty()) {
            sd = LocalDateTime.of(
                    LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(0, 0, 0)
            );
            ed = LocalDateTime.of(
                    LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE),
                    LocalTime.of(23, 59, 59)
            );
        }

        List<NoticeDto> noticeDtoList = new ArrayList<>();
        List<Notice> noticeList = noticeService.findListByDateBetween(sd, ed);
        for (Notice x : noticeList) {
            noticeDtoList.add(new NoticeDto(x));
        }

        return new Result<>(noticeDtoList);
    }

    @GetMapping("/{noticeId}")
    public Result<NoticeDto> getNotice(@PathVariable(name = "noticeId") String noticeId) {
        Optional<Notice> oneNotice = noticeService.findOneNotice(Long.parseLong(noticeId));
        return oneNotice.map(notice -> new Result<>(new NoticeDto(notice))).orElseGet(() -> new Result<>(new NoticeDto()));

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class NoticeDto {
        private Long id;
        private String title;
        private LocalDateTime date;
        private String link;
        private NoticeType type;

        public NoticeDto(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.date = notice.getDate();
            this.link = notice.getLink();
            this.type = notice.getType();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
