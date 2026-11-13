package com.ssafy.yumyum.domain.meal.types;

public enum MealCreatorType {
	USER(0), AI(20), GROUP(10);
	private int benefitPoint;

	private MealCreatorType(int benefitPoint) {
		this.benefitPoint = benefitPoint;
	}

	public int getBenefitPoint() {
		return benefitPoint;
	}
	
}
