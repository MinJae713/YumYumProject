package com.ssafy.yumyum.domain.meal.dto.response;

import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAiMealListResponse {
    // 3가지 난이도(HARD, MEDIUM, EASY)별 추천 식단 리스트
    private List<AiMealDetail> aiMeals;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AiMealDetail {
        // 기존 식단 공통 필드
        private Long mealId;
        private Double totalCalorie;
        private Double totalCarbohydrate;
        private Double totalProtein;
        private Double totalFat;

        // AI 전용 추가 필드
        private String difficulty; // HARD, MEDIUM, EASY
        private String reason;     // 추천 사유

        // 음식 목록(기존 Food DTO 재사용)
        private List<GetMealOfDateFoodDTO> foods;
    }
}
