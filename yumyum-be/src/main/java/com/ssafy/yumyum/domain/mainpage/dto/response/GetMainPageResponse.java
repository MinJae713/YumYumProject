package com.ssafy.yumyum.domain.mainpage.dto.response;

import java.util.List;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;

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
public class GetMainPageResponse {
	private List<GetRecommendMealResponse> todayRecommendMeal;
	private GetMealOfDateResponse todayMeal;
	private GetExercisesOfDateResponse todayExercise;
	private GetIntakeOfDateResponse todayIntake;
	private List<GetStreakRecordResponse> streakRecord;
	private Boolean isResearchComplete;
}
