package com.remat.domain.auth.exception;

import com.remat.global.code.ResponseCode;
import com.remat.global.exception.CustomException;

public class AuthException extends CustomException {

    public AuthException(ResponseCode responseCode) {
        super(responseCode);
    }
}
