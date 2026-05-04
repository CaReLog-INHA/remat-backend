package com.remat.domain.material.exception;

import com.remat.global.code.ResponseCode;
import com.remat.global.exception.CustomException;

public class MaterialException extends CustomException {

    public MaterialException(ResponseCode responseCode) {
        super(responseCode);
    }
}
