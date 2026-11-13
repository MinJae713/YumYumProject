package com.ssafy.yumyum.domain.mainpage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.service.ExerciseService;
import com.ssafy.yumyum.domain.mainpage.dto.response.GetMainPageResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.domain.research.service.ResearchLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService{
	private final ResearchLogService researchLogService;
	private final MealService mealService;
	private final ExerciseService exerciseService;
	private final BadgeService badgeService;

	@Override
	public GetMainPageResponse getMainPage(Long userId) {
		Boolean isResearchComplete = 
				researchLogService.getIsResearchComplete(LocalDate.now(), userId);
		List<GetRecommendMealResponse> todayRecommendMeal = 
				mealService.getRecommendMeal(userId, LocalDate.now());
		GetMealOfDateResponse todayMeal = 
				mealService.getMealOfDate(userId, LocalDate.now());
		GetExercisesOfDateResponse todayExercise = 
				exerciseService.getExercisesOfDate(userId, LocalDate.now());
		GetIntakeOfDateResponse todayIntake = 
				mealService.getIntakeOfDate(userId, LocalDate.now());
		List<GetStreakRecordResponse> streakRecord = 
				badgeService.getStreakRecord(userId);
		return GetMainPageResponse.builder()
				.isResearchComplete(isResearchComplete)
				.todayRecommendMeal(todayRecommendMeal)
				.todayMeal(todayMeal)
				.todayExercise(todayExercise)
				.todayIntake(todayIntake)
				.streakRecord(streakRecord)
				.build();
	}

	@Override
	public List<GetRecommendMealResponse> getRecommendMeal(Long userId) {
		return mealService.getRecommendMeal(userId, LocalDate.now());
	}

	@Override
	public GetMealOfDateResponse getMealOfDate(Long userId) {
		return mealService.getMealOfDate(userId, LocalDate.now());
	}

	@Override
	public GetExercisesOfDateResponse getExercisesOfDate(Long userId) {
		return exerciseService.getExercisesOfDate(userId, LocalDate.now());
	}

	@Override
	public GetIntakeOfDateResponse getIntakeOfDate(Long userId) {
		return mealService.getIntakeOfDate(userId, LocalDate.now());
	}

	@Override
	public List<GetStreakRecordResponse> getStreakRecord(Long userId) {
		return badgeService.getStreakRecord(userId);
	}
}
