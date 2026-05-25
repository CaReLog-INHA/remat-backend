package com.remat.domain.ai.service;

import com.remat.domain.material.entity.Material;
import com.remat.domain.material.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingBackfillService {

    private final MaterialRepository materialRepository;
    private final EmbeddingService embeddingService;

    /**
     * embedding이 NULL인 자재들을 일괄 임베딩 생성하여 채운다.
     * 외부 API 호출이 다수 발생하므로 트랜잭션은 자재 단위로 처리.
     */
    public BackfillResult backfillNullEmbeddings() {
        List<Material> targets = materialRepository.findAllWithNullEmbedding();
        log.info("임베딩 백필 시작: 대상 {}개", targets.size());

        int success = 0;
        int failed = 0;

        for (Material material : targets) {
            try {
                processOne(material);
                success++;
            } catch (Exception e) {
                log.warn("임베딩 백필 실패. materialId={}, name={}",
                        material.getId(), material.getMaterialName(), e);
                failed++;
            }
        }

        log.info("임베딩 백필 완료: 성공={}, 실패={}", success, failed);
        return new BackfillResult(targets.size(), success, failed);
    }

    @Transactional
    protected void processOne(Material material) {
        String text = embeddingService.buildMaterialText(
                material.getMaterialName(),
                material.getDescription(),
                material.getCategory().getDisplayName()
        );
        float[] embedding = embeddingService.generateEmbedding(text);
        material.updateEmbedding(embeddingService.toVectorString(embedding));
        materialRepository.save(material);
    }

    public record BackfillResult(int total, int success, int failed) {}
}
