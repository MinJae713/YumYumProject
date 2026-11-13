package com.ssafy.yumyum.domain.meal.dto.response;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalFoodResponse {

    @PositiveOrZero
    private Long totalPoint;

    @PositiveOrZero
    private Double totalCalorie;

    @PositiveOrZero
    private Double totalCarbohydrate;

    @PositiveOrZero
    private Double totalProtein;

    @PositiveOrZero
    private Double totalFat;
}

