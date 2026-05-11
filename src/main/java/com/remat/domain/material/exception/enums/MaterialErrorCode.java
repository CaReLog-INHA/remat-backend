package com.remat.domain.material.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaterialErrorCode implements ResponseCode {

    MATERIAL_NOT_FOUND("M404_1", "존재하지 않는 자재입니다."),
    CATEGORY_NOT_FOUND("M404_2", "존재하지 않는 카테고리입니다."),
    ;

    private final String statusCode;
    private final String message;
}
