package com.remat.domain.trade.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeErrorCode implements ResponseCode {

    MATERIAL_NOT_FOUND("T404_1", "존재하지 않는 자재입니다."),
    CANNOT_REQUEST_OWN_MATERIAL("T400_1", "본인의 자재에는 거래 요청을 할 수 없습니다."),
    RENTAL_DATE_REQUIRED("T400_2", "대여 거래는 대여 기간이 필요합니다."),
    INVALID_RENTAL_DATE("T400_3", "대여 종료일은 시작일보다 이후여야 합니다."),
    QUANTITY_EXCEEDS_STOCK("T400_4", "요청 수량이 재고를 초과합니다."),
    TRADE_NOT_FOUND("T404_2", "존재하지 않는 거래입니다."),
    NOT_TRADE_PARTICIPANT("T403_1", "해당 거래의 참여자만 리뷰를 작성할 수 있습니다."),
    ALREADY_REVIEWED("T409_1", "이미 리뷰를 작성한 거래입니다."),
    TRADE_REQUEST_NOT_FOUND("T404_3", "존재하지 않는 거래 요청입니다."),
    NOT_MATERIAL_OWNER("T403_2", "자재 소유자만 거래 요청을 처리할 수 있습니다."),
    TRADE_REQUEST_NOT_PENDING("T400_5", "대기 중인 거래 요청만 처리할 수 있습니다."),
    TRADE_ALREADY_EXISTS("T409_2", "이미 승인되어 거래가 생성된 요청입니다."),
    ;

    private final String statusCode;
    private final String message;
}
