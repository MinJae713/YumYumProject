package com.ssafy.yumyum.domain.meal.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.yumyum.domain.meal.types.MealCreatorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMealDetailResponse {

    @NotNull
    private Long mealId;

    @NotNull
    private LocalDate mealDate;

    private String mealTypeName;

    @NotBlank
    private MealCreatorType mealCreatorName;

    @PositiveOrZero
    private Integer totalPoint;
    
    @PositiveOrZero
    private Integer benefitPoint;

    @NotNull
    @Size(min = 1)
    private List<GetMealDetailFood> foods;

    // ---------------------- Food ----------------------
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMealDetailFood {

        @NotNull
        private Long foodId;

        @NotBlank
        private String name;

        @NotBlank
        private String unitName;

        @PositiveOrZero
        private Double amount;
        // 실제 먹은 수치 -> meal item에서 받아올 것

        @PositiveOrZero
        private Double calorie;

        @PositiveOrZero
        private Double carbohydrate;

        @PositiveOrZero
        private Double protein;

        @PositiveOrZero
        private Double fat;
    }
}

