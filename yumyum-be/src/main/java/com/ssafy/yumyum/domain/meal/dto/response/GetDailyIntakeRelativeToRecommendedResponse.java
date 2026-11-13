package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDailyIntakeRelativeToRecommendedResponse {

    private IntakeStatusDTO calorie;
    private IntakeStatusDTO protein;
    private IntakeStatusDTO carbohydrate;
    private IntakeStatusDTO fat;
    private IntakeStatusDTO fiber;
    private IntakeStatusDTO calcium;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IntakeStatusDTO {

        private Double intake;
        private Double recommend;
        private Double rate;
    }
}
