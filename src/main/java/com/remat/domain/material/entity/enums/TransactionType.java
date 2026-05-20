package com.remat.domain.material.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {
    SALE("판매"),
    RENTAL("대여");

    private final String description;

    public String getDescription() {
        return description;
    }
}
