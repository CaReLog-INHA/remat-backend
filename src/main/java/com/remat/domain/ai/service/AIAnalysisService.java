package com.remat.domain.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remat.domain.ai.dto.AIAnalysisResDTO;
import com.remat.domain.ai.converter.AIConverter;
import com.remat.domain.ai.dto.MatchingResult;
import com.remat.domain.ai.dto.RequiredMaterialDto;
import com.remat.domain.ai.entity.AIAnalysis;
import com.remat.domain.ai.entity.AIMatchedMaterial;
import com.remat.domain.ai.entity.AIRequiredMaterial;
import com.remat.domain.ai.exception.AIException;
import com.remat.domain.ai.exception.enums.AIErrorCode;
import com.remat.domain.ai.repository.AIAnalysisRepository;
import com.remat.domain.material.entity.Material;
import com.remat.domain.member.entity.Member;
import com.remat.global.service.R2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIAnalysisService {

    private final PdfTextExtractService pdfTextExtractService;
    private final LlmMaterialExtractService llmMaterialExtractService;
    private final MaterialMatchingService materialMatchingService;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final R2Service r2Service;
    private final ObjectMapper objectMapper;

    /**
     * 파이프라인:
     * PDF 업로드 → 텍스트 추출 → LLM 자재 추출 → 벡터 매칭 → DB 저장 → 응답 반환
     */
    public AIAnalysisResDTO.AnalysisResultDTO analyze(MultipartFile file, Member member) {
        // 1. PDF를 R2에 저장
        String fileKey = r2Service.upload(file, "ai-analysis");
        log.info("PDF 업로드 완료: {}", fileKey);

        // 2. PDF 텍스트 추출
        String pdfText = pdfTextExtractService.extract(file);
        log.info("텍스트 추출 완료: {}자", pdfText.length());

        // 3. LLM으로 필요 자재 목록 추출
        List<RequiredMaterialDto> requiredMaterials = llmMaterialExtractService.extract(pdfText);
        log.info("LLM 자재 추출 완료: {}개", requiredMaterials.size());

        // 4. 벡터 유사도 매칭
        MatchingResult matchingResult = materialMatchingService.match(requiredMaterials);

        // 5. 탄소 절감량 계산
        double totalCarbonReductionKg = calculateTotalCarbonReduction(matchingResult.matchedMaterials());

        // 6. DB 저장
        AIAnalysis analysis = saveAnalysis(
                member, fileKey, matchingResult, totalCarbonReductionKg
        );

        // 7. 이미지 URL 해결
        List<String> resolvedImageUrls = matchingResult.matchedMaterials().stream()
                .map(m -> r2Service.getFileUrl(m.getImageKey()))
                .toList();

        // 8. 응답 구성
        return AIConverter.toAnalysisResultDTO(analysis.getId(), matchingResult, totalCarbonReductionKg, resolvedImageUrls);
    }

    @Transactional
    protected AIAnalysis saveAnalysis(
            Member member,
            String fileKey,
            MatchingResult matchingResult,
            double totalCarbonReductionKg
    ) {
        String analysisResultJson = serializeAnalysisResult(
                matchingResult.matchedMaterials().size(), totalCarbonReductionKg
        );

        AIAnalysis analysis = AIAnalysis.builder()
                .member(member)
                .fileKey(fileKey)
                .matchedCount(matchingResult.matchedMaterials().size())
                .analysisResult(analysisResultJson)
                .build();

        // 매칭된 자재 연결
        for (Material material : matchingResult.matchedMaterials()) {
            AIMatchedMaterial matched = AIMatchedMaterial.builder()
                    .matchedMaterial(material)
                    .build();
            analysis.addMatchedMaterial(matched);
        }

        // 미매칭 자재 저장
        for (RequiredMaterialDto unmatched : matchingResult.unmatchedMaterials()) {
            AIRequiredMaterial required = AIRequiredMaterial.builder()
                    .materialName(unmatched.name())
                    .description(unmatched.description() != null ? unmatched.description() : "")
                    .build();
            analysis.addRequiredMaterial(required);
        }

        return aiAnalysisRepository.save(analysis);
    }

    private double calculateTotalCarbonReduction(List<Material> materials) {
        return materials.stream()
                .mapToDouble(m -> {
                    double avgWeightKg = m.getCategory().getAvgWeightKg();
                    int esgEffect = m.getCategory().getEsgEffect(); // g CO2/kg
                    return avgWeightKg * esgEffect / 1000.0;        // → kg CO2
                })
                .sum();
    }

    private String serializeAnalysisResult(int matchedCount, double totalCarbonReductionKg) {
        try {
            return objectMapper.writeValueAsString(
                    Map.of("matchedCount", matchedCount,
                           "totalCarbonReductionKg", totalCarbonReductionKg)
            );
        } catch (Exception e) {
            throw new AIException(AIErrorCode.LLM_PARSE_FAILED, e);
        }
    }
}
