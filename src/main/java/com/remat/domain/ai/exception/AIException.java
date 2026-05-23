package com.remat.domain.ai.exception;

import com.remat.global.code.ResponseCode;
import com.remat.global.exception.CustomException;

public class AIException extends CustomException {

    public AIException(ResponseCode responseCode) {
        super(responseCode);
    }

    public AIException(ResponseCode responseCode, Throwable cause) {
        super(responseCode, responseCode.getMessage(), cause);
    }
}
