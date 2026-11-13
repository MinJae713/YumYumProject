package com.ssafy.yumyum.domain.meal.service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealInfoDTO {
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
