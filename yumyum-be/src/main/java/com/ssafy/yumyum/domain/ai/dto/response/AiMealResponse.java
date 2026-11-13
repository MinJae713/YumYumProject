package com.ssafy.yumyum.domain.ai.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AiMealResponse {
    // 추천 식단에 대한 AI의 코멘트
    private String aiComment;
    // 3가지 타입(HARD, MEDIUM, EASY)별 추천 식단 리스트
    private List<Recommendation> recommendations;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Recommendation{
        private String difficulty;      // HARD, MEDIUM, EASY
        private String reason;          // 추천 이유 -> 키워드
        private List<AiFoodItem> items; // 식단 음식 리스트
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class AiFoodItem{
        private String foodName;    // 음식 이름
        private int amount;         // 음식 량(g) (meal_item은 decimal)
    }
}
