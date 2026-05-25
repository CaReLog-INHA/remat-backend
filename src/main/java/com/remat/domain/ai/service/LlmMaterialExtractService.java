package com.remat.domain.ai.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.remat.domain.ai.dto.RequiredMaterialDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmMaterialExtractService {

    private final RestClient openAiRestClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model}")
    private String model;

    private static final int MAX_TEXT_LENGTH = 100_000;

    private static final String SYSTEM_PROMPT = """
            당신은 행사, 전시, 프로젝트 기획서를 분석하여 필요한 자재 목록을 추출하는 전문가입니다.
            기획서 텍스트를 분석하고, 반드시 아래 JSON 형식으로만 응답하세요. 다른 텍스트는 포함하지 마세요.

            응답 형식:
            {
              "materials": [
                {
                  "name": "자재명",
                  "description": "용도 및 설명",
                  "quantity": 수량(숫자, 불명확하면 null),
                  "specs": "규격 정보(없으면 null)"
                }
              ]
            }

            추출 규칙:
            - 물리적으로 필요한 자재, 장비, 비품만 추출하세요.
            - 인력, 서비스, 프로그램 항목은 제외하세요.
            - 동일 자재가 여러 번 언급되면 하나로 통합하세요.
            - name은 한국어로 간결하게 작성하세요.
            """;

    public List<RequiredMaterialDto> extract(String pdfText) {
        String truncatedText = truncate(pdfText);

        ChatRequest request = buildRequest(truncatedText);
        String responseBody = callOpenAi(request);
        return parseResponse(responseBody);
    }

    private String truncate(String text) {
        if (text.length() > MAX_TEXT_LENGTH) {
            log.warn("PDF 텍스트가 최대 길이를 초과하여 잘라냅니다. 원본: {}자, 최대: {}자", text.length(), MAX_TEXT_LENGTH);
            return text.substring(0, MAX_TEXT_LENGTH);
        }
        return text;
    }

    private ChatRequest buildRequest(String pdfText) {
        String userMessage = "다음 기획서에서 필요한 자재 목록을 추출해주세요:\n\n" + pdfText;

        return new ChatRequest(
                model,
                List.of(
                        new Message("system", SYSTEM_PROMPT),
                        new Message("user", userMessage)
                ),
                new ResponseFormat("json_object"),
                0.2
        );
    }

    private String callOpenAi(ChatRequest request) {
        try {
            return openAiRestClient.post()
                    .uri("/v1/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            log.error("OpenAI API 호출 실패", e);
            throw new AIException(AIErrorCode.LLM_CALL_FAILED, e);
        }
    }

    private List<RequiredMaterialDto> parseResponse(String responseBody) {
        try {
            // json -> java 객체 역직렬화
            ChatResponse chatResponse = objectMapper.readValue(responseBody, ChatResponse.class);
            String content = chatResponse.choices().get(0).message().content();

            log.debug("LLM 응답 content: {}", content);

            MaterialsResult result = objectMapper.readValue(content, MaterialsResult.class);

            if (result.materials() == null || result.materials().isEmpty()) {
                throw new AIException(AIErrorCode.NO_MATERIALS_EXTRACTED);
            }

            log.info("자재 추출 완료: {}개", result.materials().size());
            return result.materials();

        } catch (AIException e) {
            throw e;
        } catch (Exception e) {
            log.error("LLM 응답 파싱 실패. 응답: {}", responseBody, e);
            throw new AIException(AIErrorCode.LLM_PARSE_FAILED, e);
        }
    }

    // ── OpenAI API 요청/응답 내부 레코드 ──────────────────────────────

    private record ChatRequest(
            String model,
            List<Message> messages,
            ResponseFormat response_format,
            double temperature
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Message(
            String role,
            String content
    ) {}

    private record ResponseFormat(String type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ChatResponse(List<Choice> choices) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record Choice(Message message) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record MaterialsResult(List<RequiredMaterialDto> materials) {}
}
