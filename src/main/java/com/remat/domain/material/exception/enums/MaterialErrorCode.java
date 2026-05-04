package com.remat.domain.material.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialErrorCode implements ResponseCode {

    MATERIAL_NOT_FOUND("M404_1", "존재하지 않는 자재입니다."),
    CATEGORY_NOT_FOUND("M404_2", "존재하지 않는 카테고리입니다."),
    INVALID_MATERIAL_CONDITION("M400_1", "올바르지 않은 자재 상태입니다."),
    INVALID_TRANSACTION_TYPE("M400_2", "올바르지 않은 거래 유형입니다."),
    INVALID_REGION_NAME("M400_3", "지역명이 올바르지 않습니다."),
    ;

    private final String statusCode;
    private final String message;
}
