package com.remat.domain.ai.controller;

import com.remat.domain.ai.controller.docs.EmbeddingBackfillApi;
import com.remat.domain.ai.service.EmbeddingBackfillService;
import com.remat.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/embeddings")
@RequiredArgsConstructor
public class EmbeddingBackfillController implements EmbeddingBackfillApi {

    private final EmbeddingBackfillService embeddingBackfillService;

    @Override
    @PostMapping("/backfill")
    public ApiResponse<EmbeddingBackfillService.BackfillResult> backfill() {
        EmbeddingBackfillService.BackfillResult result =
                embeddingBackfillService.backfillNullEmbeddings();
        return ApiResponse.ok(result);
    }
}
