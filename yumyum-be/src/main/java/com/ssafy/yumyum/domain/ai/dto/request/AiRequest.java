package com.ssafy.yumyum.domain.ai.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequest {

    private String model;
    private List<Message> messages;

    // Java에서는 camelCase로 쓰지만, JSON으로 직렬화될 때는 "response_format"으로 변환됨
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    // 🎯 결정론적 출력 강화 파라미터 추가
    private Double temperature;           // 0.0 ~ 2.0 (낮을수록 결정론적)

    @JsonProperty("top_p")
    private Double topP;                  // 0.0 ~ 1.0 (nucleus sampling)

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;      // -2.0 ~ 2.0 (반복 억제)

    @JsonProperty("presence_penalty")
    private Double presencePenalty;       // -2.0 ~ 2.0 (새 주제 등장)

    private Integer seed;                 // 재현성 보장 (실험적 기능)

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    // response_format의 구조 정의 ({ "type": "json_object" })
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseFormat {
        private String type;
    }
}
