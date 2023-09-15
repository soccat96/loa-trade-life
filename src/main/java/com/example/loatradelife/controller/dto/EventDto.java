package com.example.loatradelife.controller.dto;

import com.example.loatradelife.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String link;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime rewardDate;

    public EventDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.thumbnail = event.getThumbnail();
        this.link = event.getLink();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.rewardDate = event.getRewardDate();
    }
}
