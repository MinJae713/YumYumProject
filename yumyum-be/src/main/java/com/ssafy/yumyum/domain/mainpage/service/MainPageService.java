package com.ssafy.yumyum.domain.mainpage.service;

import java.util.List;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.mainpage.dto.response.GetMainPageResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;

public interface MainPageService {
	GetMainPageResponse getMainPage(Long userId);
	List<GetRecommendMealResponse> getRecommendMeal(Long userId);
	GetMealOfDateResponse getMealOfDate(Long userId);
	GetExercisesOfDateResponse getExercisesOfDate(Long userId);
	GetIntakeOfDateResponse getIntakeOfDate(Long userId);
	List<GetStreakRecordResponse> getStreakRecord(Long userId);
}
