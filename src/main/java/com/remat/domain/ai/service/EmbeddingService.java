package com.remat.domain.ai.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remat.domain.ai.exception.AIException;
import com.remat.domain.ai.exception.enums.AIErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final RestClient openAiRestClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    /**
     * 텍스트를 OpenAI Embeddings API로 변환해 float[] 벡터를 반환한다.
     *
     * 동작 원리:
     * 1. 입력 텍스트를 OpenAI text-embedding-3-small 모델에 전송
     * 2. 모델이 텍스트의 의미를 압축한 1536차원 float 배열 반환
     * 3. 의미가 유사한 텍스트일수록 두 벡터의 코사인 유사도가 1에 가까워짐
     */
    public float[] generateEmbedding(String text) {
        EmbeddingRequest request = new EmbeddingRequest(embeddingModel, text);

        String responseBody;
        try {
            responseBody = openAiRestClient.post()
                    .uri("/v1/embeddings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            log.error("OpenAI Embeddings API 호출 실패. text 길이: {}", text.length(), e);
            throw new AIException(AIErrorCode.LLM_CALL_FAILED, e);
        }

        return parseEmbedding(responseBody);
    }

    /**
     * 자재 정보를 임베딩 입력 텍스트로 조합한다.
     * 모델이 자재의 의미를 최대한 파악할 수 있도록 핵심 필드를 조합.
     */
    public String buildMaterialText(String name, String description, String categoryName) {
        return name + " " + categoryName + " " + description;
    }

    /**
     * float[] 벡터를 pgvector가 읽을 수 있는 문자열로 변환한다.
     * 예: [0.12, -0.34, 0.87, ...] 형태로 반환함.
     */
    public String toVectorString(float[] embedding) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (float v : embedding) {
            joiner.add(String.valueOf(v));
        }
        return joiner.toString();
    }

    private float[] parseEmbedding(String responseBody) {
        try {
            EmbeddingResponse response = objectMapper.readValue(responseBody, EmbeddingResponse.class);
            List<Double> values = response.data().get(0).embedding();

            float[] result = new float[values.size()];
            for (int i = 0; i < values.size(); i++) {
                result[i] = values.get(i).floatValue();
            }

            log.debug("임베딩 생성 완료: {}차원", result.length);
            return result;

        } catch (Exception e) {
            log.error("임베딩 응답 파싱 실패", e);
            throw new AIException(AIErrorCode.LLM_PARSE_FAILED, e);
        }
    }

    // ── OpenAI Embeddings API 요청/응답 레코드 ──────────────────────────

    private record EmbeddingRequest(String model, String input) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingResponse(List<EmbeddingData> data) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record EmbeddingData(List<Double> embedding) {}
}
