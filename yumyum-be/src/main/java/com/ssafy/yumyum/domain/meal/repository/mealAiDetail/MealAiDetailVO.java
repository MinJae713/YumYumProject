package com.ssafy.yumyum.domain.meal.repository.mealAiDetail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealAiDetailVO {
    private Long mealId;
    private String difficulty;
    private String reason;
}
