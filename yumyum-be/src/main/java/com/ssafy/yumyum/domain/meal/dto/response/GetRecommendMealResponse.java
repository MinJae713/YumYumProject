package com.ssafy.yumyum.domain.meal.dto.response;

import java.util.List;

import com.ssafy.yumyum.domain.meal.service.dto.GetMealOfDateFoodDTO;
import com.ssafy.yumyum.domain.meal.types.MealCreatorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class GetRecommendMealResponse {

	@NotNull
    private Long mealId;

    @NotBlank
    private MealCreatorType mealCreatorName;

    private String title; // 더미로 지정

    private String description; // 더미로 지정

    @PositiveOrZero
    private Double totalCalorie;

    @PositiveOrZero
    private Double totalCarbohydrate;

    @PositiveOrZero
    private Double totalProtein;

    @PositiveOrZero
    private Double totalFat;
    
    @PositiveOrZero
    private Integer totalPoint;
    
    @PositiveOrZero
    private Integer benefitPoint;

    @NotNull
    private List<GetMealOfDateFoodDTO> foods;
}
