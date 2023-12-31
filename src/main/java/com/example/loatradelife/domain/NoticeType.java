package com.example.loatradelife.domain;

public enum NoticeType {
    NOTICE, INSPECTION, SHOP, EVENT, ETC;

    public static NoticeType getNoticeType(String type) {
        switch (type) {
            case "공지"   -> { return NoticeType.NOTICE; }
            case "점검"   -> { return NoticeType.INSPECTION; }
            case "상점"   -> { return NoticeType.SHOP; }
            case "이벤트" -> { return NoticeType.EVENT; }
            case "기타"   -> { return NoticeType.ETC; }
        }

        return NoticeType.NOTICE;
    }
}
