package com.remat.domain.ai.service;

import com.remat.domain.ai.dto.MatchingResult;
import com.remat.domain.ai.dto.RequiredMaterialDto;
import com.remat.domain.material.entity.Material;
import com.remat.domain.material.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialMatchingService {

    private final MaterialRepository materialRepository;
    private final EmbeddingService embeddingService;

    // 코사인 거리 임계값: 0.4 이하면 매칭
    private static final double SIMILARITY_THRESHOLD = 0.4;
    // 각 자재당 유사도 검색 후보 수
    private static final int TOP_K = 3;

    public MatchingResult match(List<RequiredMaterialDto> requiredMaterials) {
        Set<Long> matchedIds = new LinkedHashSet<>();
        List<RequiredMaterialDto> unmatched = new ArrayList<>();

        for (RequiredMaterialDto required : requiredMaterials) {
            String text = buildSearchText(required);
            float[] embedding = embeddingService.generateEmbedding(text);
            String vectorStr = embeddingService.toVectorString(embedding);

            List<MaterialRepository.SimilarityProjection> candidates =
                    materialRepository.findSimilarMaterials(vectorStr, TOP_K);

            Optional<Long> bestMatchId = candidates.stream()
                    .filter(c -> c.getDistance() != null && c.getDistance() < SIMILARITY_THRESHOLD)
                    .map(MaterialRepository.SimilarityProjection::getId)
                    .findFirst(); // 이미 distance 오름차순 정렬

            if (bestMatchId.isPresent()) {
                Long id = bestMatchId.get();
                boolean isNew = matchedIds.add(id); // 이미 추가된 ID면 false
                log.debug("매칭 성공: required='{}' → materialId={} (중복={})", required.name(), id, !isNew);
            } else {
                unmatched.add(required);
                log.debug("매칭 실패: required='{}' → 미매칭", required.name());
            }
        }

        List<Material> matchedMaterials = matchedIds.isEmpty()
                ? List.of()
                : materialRepository.findAllByIdIn(matchedIds);

        log.info("매칭 완료: 전체={}, 매칭={}, 미매칭={}",
                requiredMaterials.size(), matchedMaterials.size(), unmatched.size());

        return new MatchingResult(matchedMaterials, unmatched);
    }

    private String buildSearchText(RequiredMaterialDto required) {
        StringBuilder sb = new StringBuilder(required.name());
        if (required.description() != null && !required.description().isBlank()) {
            sb.append(" ").append(required.description());
        }
        if (required.specs() != null && !required.specs().isBlank()) {
            sb.append(" ").append(required.specs());
        }
        return sb.toString();
    }
}
