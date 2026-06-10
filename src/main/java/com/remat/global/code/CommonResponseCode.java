package com.remat.global.code;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonResponseCode implements ResponseCode {

    OK(HttpStatus.OK, "C000", "success"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "C001", "api bad request exception"),
    REQUEST_BODY_MISSING_ERROR(HttpStatus.BAD_REQUEST, "C002", "required request body is missing"),
    MISSING_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "C003", "missing servlet requestParameter exception"),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "C004", "forbidden exception"),
    NULL_POINT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "null point exception"),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "C006", "not found exception"),
    NOT_VALID_ERROR(HttpStatus.BAD_REQUEST, "C007", "handle validation exception"),
    NOT_VALID_HEADER_ERROR(HttpStatus.BAD_REQUEST, "C008", "not valid header exception"),
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "C009", "external api request failed"),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "C010", "unauthorized exception"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "internal server error exception")
    ;

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String message;
}
