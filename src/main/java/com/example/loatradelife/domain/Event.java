package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    private String title;
    private String thumbnail;
    private String link;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime rewardDate;

    public Event(String title, String thumbnail, String link, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime rewardDate) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.link = link;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rewardDate = rewardDate;
    }

    public void update(Event event) {
        this.title = event.getTitle();
        this.thumbnail = event.getThumbnail();
        this.link = event.getLink();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.rewardDate = event.getRewardDate();
    }
}
