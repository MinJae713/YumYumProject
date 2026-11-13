package com.ssafy.yumyum.domain.meal.repository.meal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MealVO {
    private Long mealId;
    private LocalDate mealDate;
    private Double totalKcal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer point;
    private Boolean isRecommendation;
    private Long userId;
    private Long mealTypeId;
    private Long mealCreatorTypeId;
}
