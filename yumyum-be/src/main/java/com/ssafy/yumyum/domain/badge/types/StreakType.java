package com.ssafy.yumyum.domain.badge.types;

public enum StreakType {
	MEAL_RECORD("식단"),
	EXERCISE_RECORD("운동");
	private String name;
	private StreakType(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
