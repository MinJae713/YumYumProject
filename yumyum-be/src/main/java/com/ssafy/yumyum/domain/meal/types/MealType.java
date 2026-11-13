package com.ssafy.yumyum.domain.meal.types;

import com.ssafy.yumyum.domain.meal.exception.NoMealTypeException;

public enum MealType {
	BREAKFAST("아침"),
	LUNCH("점심"),
	DINNER("저녁"),
	SNACK("간식");
	private String label;
	private MealType(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public static MealType getMealTypeFromLabel(String label) {
		for (MealType mealType : MealType.values())
			if (mealType.getLabel().equals(label))
				return mealType;
		throw new NoMealTypeException();
	}
}
