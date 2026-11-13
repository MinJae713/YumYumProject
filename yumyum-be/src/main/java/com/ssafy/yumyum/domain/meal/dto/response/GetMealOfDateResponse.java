package com.ssafy.yumyum.domain.meal.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;
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
public class GetMealOfDateResponse {

    @PositiveOrZero
    private Double mealTotalCalorie;

    @PositiveOrZero
    private Integer mealTotalPoint;
    
    @PositiveOrZero
    private Integer mealTotalBenefitPoint;
    
    private List<LocalDate> recordedDates; // 추가

    @NotNull
    @Size(min = 1)
    private List<GetMealOfDateMeal> meals;

    // ---------------------- Meal ----------------------
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMealOfDateMeal {

    	@NotNull
        private Long mealId;

        @NotBlank
        private String mealTypeName;

        @NotBlank
        private MealCreatorType mealCreatorName;

        @PositiveOrZero
        private Integer totalPoint;
        
        @PositiveOrZero
        private Integer benefitPoint;

        @PositiveOrZero
        private Double totalCalorie;

        @PositiveOrZero
        private Double totalCarbohydrate;

        @PositiveOrZero
        private Double totalProtein;

        @PositiveOrZero
        private Double totalFat;

        @NotNull
        private List<GetMealOfDateFoodDTO> foods;
    }
}


