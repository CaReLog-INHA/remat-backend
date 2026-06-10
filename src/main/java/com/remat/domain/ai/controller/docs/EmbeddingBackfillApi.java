package com.remat.domain.ai.controller.docs;

import com.remat.domain.ai.service.EmbeddingBackfillService;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 관리자 임베딩 백필 API의 Swagger 문서 전용 인터페이스.
 */
@Tag(name = "관리자 - 임베딩 백필", description = "기존 자재의 임베딩을 일괄 생성합니다")
public interface EmbeddingBackfillApi {

    @Operation(
            summary = "임베딩이 없는 자재 일괄 백필",
            description = "embedding 컬럼이 NULL인 모든 자재에 대해 OpenAI 임베딩을 생성하여 저장합니다."
    )
    ApiResponse<EmbeddingBackfillService.BackfillResult> backfill();
}
