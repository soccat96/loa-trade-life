package com.example.loatradelife.controller.dto;

import com.example.loatradelife.domain.Notice;
import com.example.loatradelife.domain.NoticeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
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
