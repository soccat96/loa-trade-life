package com.example.loatradelife.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_grade_id")
    private Long id;

    private Integer number;
    private String name;
    private Boolean useAt;

    public ItemGrade(Integer number, String name) {
        this.number = number;
        this.name = name;
        this.useAt = true;
    }

    public ItemGrade(Integer number, String name, Boolean useAt) {
        this.number = number;
        this.name = name;
        this.useAt = useAt;
    }

    public void update(ItemGrade itemGrade) {
        this.number = itemGrade.getNumber();
        this.name = itemGrade.getName();
        this.useAt = itemGrade.getUseAt();
    }

    public void changeUseAt(boolean useAt) {
        this.useAt = useAt;
    }
}
