package com.remat.domain.trade.exception;

import com.remat.global.code.ResponseCode;
import com.remat.global.exception.CustomException;

public class TradeException extends CustomException {

    public TradeException(ResponseCode responseCode) {
        super(responseCode);
    }
}
