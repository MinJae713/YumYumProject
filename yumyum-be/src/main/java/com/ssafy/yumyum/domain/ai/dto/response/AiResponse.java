package com.ssafy.yumyum.domain.ai.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AiResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
