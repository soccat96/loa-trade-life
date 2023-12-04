package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    private LocalDateTime date;
    private String link;

    @Enumerated(EnumType.STRING)
    private NoticeType type;

    @OneToOne
    @JoinColumn(name = "inspection_time_id")
    private InspectionTime inspectionTime;

    public Notice(String title, LocalDateTime date, String link, NoticeType type) {
        this.title = title;
        this.date = date;
        this.link = link;
        this.type = type;
    }

    public void updateNotice(Notice notice) {
        this.title = notice.getTitle();
        this.date = notice.getDate();
        this.link = notice.getLink();
        this.type = notice.getType();
    }
}
