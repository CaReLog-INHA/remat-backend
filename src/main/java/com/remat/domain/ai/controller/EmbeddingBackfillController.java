package com.remat.domain.ai.controller;

import com.remat.domain.ai.service.EmbeddingBackfillService;
import com.remat.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 - 임베딩 백필", description = "기존 자재의 임베딩을 일괄 생성합니다")
@RestController
@RequestMapping("/admin/embeddings")
@RequiredArgsConstructor
public class EmbeddingBackfillController {

    private final EmbeddingBackfillService embeddingBackfillService;

    @Operation(
            summary = "임베딩이 없는 자재 일괄 백필",
            description = "embedding 컬럼이 NULL인 모든 자재에 대해 OpenAI 임베딩을 생성하여 저장합니다."
    )
    @PostMapping("/backfill")
    public ApiResponse<EmbeddingBackfillService.BackfillResult> backfill() {
        EmbeddingBackfillService.BackfillResult result =
                embeddingBackfillService.backfillNullEmbeddings();
        return ApiResponse.ok(result);
    }
}
