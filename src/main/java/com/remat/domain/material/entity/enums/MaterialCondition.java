package com.remat.domain.material.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MaterialCondition {
    BEST("최상"),
    GOOD("양호"),
    NORMAL("보통");

    private final String description;

    public String getDescription() {
        return description;
    }
}
