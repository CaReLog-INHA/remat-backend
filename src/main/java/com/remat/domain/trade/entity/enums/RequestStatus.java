package com.remat.domain.trade.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RequestStatus {
    PENDING("대기"),
    ACCEPTED("승인"),
    REJECTED("거절");

    private String description;

    public String getDescription() {
        return description;
    }
}
