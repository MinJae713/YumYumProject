package com.ssafy.yumyum.domain.meal.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealTargetNutrionDTO {
	private Double mealCarbTarget;
	private Double mealProteinTarget;
	private Double mealFatTarget;
}
