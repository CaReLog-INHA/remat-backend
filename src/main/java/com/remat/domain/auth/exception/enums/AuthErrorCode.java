package com.remat.domain.auth.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ResponseCode {

    NOT_EQUAL_PASSWORD("A400_1", "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL("A400_2", "이메일이 중복입니다."),
    INVALID_REFRESH_TOKEN("A400_3", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_LOGIN_CREDENTIALS("A400_4", "비밀번호가 틀렸습니다."),
    ;

    private final String statusCode;
    private final String message;
}
