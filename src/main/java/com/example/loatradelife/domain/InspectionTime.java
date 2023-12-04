package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InspectionTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_time_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public InspectionTime(Notice notice, LocalDateTime startTime, LocalDateTime endTime) {
        this.notice = notice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void updateTimes(LocalDateTime start, LocalDateTime end) {
        this.startTime = start;
        this.endTime = end;
    }

    public boolean isBetween(LocalDateTime localDateTime) {
        return localDateTime.isAfter(this.startTime) && localDateTime.isBefore(this.endTime);
    }
}