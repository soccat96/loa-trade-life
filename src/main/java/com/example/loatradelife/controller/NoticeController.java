package com.example.loatradelife.controller;

import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.domain.NoticeType;
import com.example.loatradelife.service.NoticeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Map<String, LocalDateTime> mapSdEd = StartDateEndDateMaker.getStartDateEndDate(startDate, endDate);

        List<NoticeDto> noticeDtoList = new ArrayList<>();
        List<Notice> noticeList = noticeService.findListByDateBetween(mapSdEd.get("sd"), mapSdEd.get("ed"));
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
}
