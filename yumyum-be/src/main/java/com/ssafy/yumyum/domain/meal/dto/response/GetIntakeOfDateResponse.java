package com.ssafy.yumyum.domain.meal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetIntakeOfDateResponse {

    private IntakeValue calorie;
    private IntakeValue carbohydrate;
    private IntakeValue protein;
    private IntakeValue fat;
    private IntakeValue fiber;
    private IntakeValue calcium;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntakeValue {
        private Double intake;
        private Double target;
        private Double rate;
        private String unitName;
    }
}
