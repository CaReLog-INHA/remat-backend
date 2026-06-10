package com.remat.domain.auth.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ResponseCode {

    NOT_EQUAL_PASSWORD(HttpStatus.BAD_REQUEST, "A400_1", "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "A400_2", "이메일이 중복입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "A400_3", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.BAD_REQUEST, "A400_4", "비밀번호가 틀렸습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String message;
}
