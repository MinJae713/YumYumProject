package com.ssafy.yumyum.domain.meal.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiMealHeaderDTO {
    private Long mealId;
    private Double totalKcal;
    private String difficulty;
    private String reason;
}
