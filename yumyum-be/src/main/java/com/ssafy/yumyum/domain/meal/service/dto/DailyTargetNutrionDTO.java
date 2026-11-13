package com.ssafy.yumyum.domain.meal.service.dto;

import lombok.Getter;

@Getter
public class DailyTargetNutrionDTO {
	private Double dailyCarbTarget;
	private Double dailyProteinTarget;
	private Double dailyFatTarget;
	
	private final Double carbRate = 0.50;
	private final Double proteinRate = 0.20;
	private final Double fatRate = 0.30;
	
	private final Double carbKcalPerGram = 4.0; // 4kcal/g
	private final Double proteinKcalPerGram = 4.0; // 4kcal/g
	private final Double fatKcalPerGram = 9.0; // 9kcal/g
	// 권장량 비율
	private final Double mealRate = 0.3;
	private final Double snackRate = 0.1;
	
	public DailyTargetNutrionDTO(Double dailyKcalTarget) {
		dailyCarbTarget = (dailyKcalTarget * carbRate) / carbKcalPerGram;
		dailyProteinTarget = (dailyKcalTarget * proteinRate) / proteinKcalPerGram;
		dailyFatTarget = (dailyKcalTarget * fatRate) / fatKcalPerGram;
	}
	
	// 탄단지 한 끼 권장량 계산
	public MealTargetNutrionDTO getMealTargetNutrion(Boolean isSnack) {
		// 한 끼 권장량 -> 식사라면 0.3, 간식이라면 0.1 곱해서 구함
		Double mealCarbTarget = dailyCarbTarget * (isSnack ? snackRate : mealRate); // 탄수화물 한 끼 권장량
		Double mealProteinTarget = dailyProteinTarget * (isSnack ? snackRate : mealRate); // 단백질 한 끼 권장량
		Double mealFatTarget = dailyFatTarget * (isSnack ? snackRate : mealRate); // 지방 한 끼 권장량
		return MealTargetNutrionDTO.builder()
				.mealCarbTarget(mealCarbTarget)
				.mealProteinTarget(mealProteinTarget)
				.mealFatTarget(mealFatTarget)
				.build();
	}
}
