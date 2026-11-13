package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetWeeklyDailyNutrionIntakeRateResponse {

    private PeriodNutritionDTO weekly;
    private PeriodNutritionDTO daily;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PeriodNutritionDTO {

        private Double totalIntakeCalorie;
        private NutrientDTO carbohydrate;
        private NutrientDTO protein;
        private NutrientDTO fat;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NutrientDTO {

        private Double intakeCalorie;
        private Double rate;
    }
}
