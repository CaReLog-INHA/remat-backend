package com.remat.domain.ai.exception.enums;

import com.remat.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AIErrorCode implements ResponseCode {

    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "AI400_1", "PDF 파일만 업로드 가능합니다."),
    FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "AI400_2", "파일 크기는 50MB를 초과할 수 없습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "AI400_3", "파일이 비어있습니다."),
    PDF_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI500_1", "PDF 파일을 읽는 중 오류가 발생했습니다."),
    PDF_EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "AI400_4", "PDF에서 텍스트를 추출할 수 없습니다. 스캔된 이미지 PDF는 지원하지 않습니다."),
    LLM_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI500_2", "AI 분석 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    LLM_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI500_3", "AI 응답을 처리하는 중 오류가 발생했습니다."),
    NO_MATERIALS_EXTRACTED(HttpStatus.BAD_REQUEST, "AI400_5", "기획서에서 자재 정보를 찾을 수 없습니다."),
    ANALYSIS_NOT_FOUND(HttpStatus.NOT_FOUND, "AI404_1", "존재하지 않는 분석 결과입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String message;
}
