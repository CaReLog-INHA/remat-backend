package com.remat.global.code;

import org.springframework.http.HttpStatus;

public interface ResponseCode {
    HttpStatus getHttpStatus();
    String getStatusCode();
    String getMessage();
}
