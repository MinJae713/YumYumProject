package com.ssafy.yumyum.domain.badge.types;

public enum CommandType {
	ADD_MEAL(new BadgeType[] {
		BadgeType.MEAL_STREAK_RECORD_WEEK,
		BadgeType.MEAL_STREAK_RECORD_FULL_MOON,
		BadgeType.MEAL_STREAK_RECORD_MONTH,
		
		BadgeType.FIRST_MEAL_RECORD,
		BadgeType.NUTRIENT_EXPERT,
		
		BadgeType.ACHIEVE_GROUP_THIRD,
		BadgeType.ACHIEVE_GROUP_NO_1,
	}),
	ADD_EXERCISE(new BadgeType[] {
		BadgeType.EXERCISE_STREAK_RECORD_WEEK,
		BadgeType.EXERCISE_STREAK_RECORD_FULL_MOON,
		BadgeType.EXERCISE_STREAK_RECORD_MONTH,
		
		BadgeType.FIRST_EXERCISE_RECORD,
		
		BadgeType.ACHIEVE_GROUP_THIRD,
		BadgeType.ACHIEVE_GROUP_NO_1,
	}),
	JOIN(new BadgeType[] {
		BadgeType.FIRST_SIGN_UP
	}),
	JOIN_GROUP(new BadgeType[] {
		BadgeType.FIRST_SIGN_UP_GROUP
	}),
	GROUP_LEADER_BADGE(new BadgeType[] {
		BadgeType.WELL_DONE
	}),
	APPLY_RECOMMEND_MEAL_AI(new BadgeType[] {
		BadgeType.MEAL_STREAK_RECORD_WEEK,
		BadgeType.MEAL_STREAK_RECORD_FULL_MOON,
		BadgeType.MEAL_STREAK_RECORD_MONTH,
		
		BadgeType.FIRST_MEAL_RECORD,
		BadgeType.NUTRIENT_EXPERT,
		
		BadgeType.ACHIEVE_GROUP_THIRD,
		BadgeType.ACHIEVE_GROUP_NO_1,
		
		BadgeType.SELECT_MEAL_AI_10
	}),
	APPLY_RECOMMEND_MEAL_GROUP(new BadgeType[] {
		BadgeType.MEAL_STREAK_RECORD_WEEK,
		BadgeType.MEAL_STREAK_RECORD_FULL_MOON,
		BadgeType.MEAL_STREAK_RECORD_MONTH,
		
		BadgeType.FIRST_MEAL_RECORD,
		BadgeType.NUTRIENT_EXPERT,
		
		BadgeType.ACHIEVE_GROUP_THIRD,
		BadgeType.ACHIEVE_GROUP_NO_1,
		
		BadgeType.SELECT_MEAL_GROUP_10
	});
	private BadgeType[] badgeTypes;
	private CommandType(BadgeType[] badgeTypes) {
		this.badgeTypes = badgeTypes;
	}
	public BadgeType[] getBadgeTypes() {
		return badgeTypes;
	}
}
