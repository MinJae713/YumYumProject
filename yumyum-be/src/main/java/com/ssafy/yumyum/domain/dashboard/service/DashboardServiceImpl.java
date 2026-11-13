package com.ssafy.yumyum.domain.dashboard.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.BadgeService;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.dashboard.dto.response.GetDashboardPageResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.exercise.service.ExerciseService;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.meal.service.MealService;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;
import com.ssafy.yumyum.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
	
	private final BadgeService badgeService;
	private final UserService userService;
	private final MealService mealService;
	private final ExerciseService exerciseService;

	@Override
	public GetDashboardPageResponse getDashboardPage(Long userId) {
		List<GetBadgeListBadgeDTO> recentBadgeList = getRecentBadgeList(userId);
		GetWeightDeltaResponse weightDelta = getWeightDelta(userId);
		GetWeeklyDailyNutrionIntakeRateResponse weeklyDailyNutrionIntakeRate = 
				getWeeklyDailyNutrionIntakeRate(userId);
		GetIntakeOfDateResponse dailyIntakeRelativeToRecommended = 
				getDailyIntakeRelativeToRecommended(userId);
		GetDailyAverageSpendedCalorieResponse dailyAverageSpendedCalorie = 
				getDailyAverageSpendedCalorie(userId);
		GetWeeklyTotalDurationResponse weeklyTotalDuration = 
				getWeeklyTotalDuration(userId);
		List<GetStreakRecordResponse> streakRecordList = 
				getStreakRecord(userId);
		return GetDashboardPageResponse.builder()
				.recentBadgeList(recentBadgeList)
				.weightDelta(weightDelta)
				.weeklyDailyNutrionIntakeRate(weeklyDailyNutrionIntakeRate)
				.dailyIntakeRelativeToRecommended(dailyIntakeRelativeToRecommended)
				.dailyAverageSpendedCalorie(dailyAverageSpendedCalorie)
				.weeklyTotalDuration(weeklyTotalDuration)
				.streakRecordList(streakRecordList)
				.build();
	}

	@Override
	public List<GetBadgeListBadgeDTO> getRecentBadgeList(Long userId) {
		return badgeService.getRecentBadgeList(userId);
	}

	@Override
	public GetWeightDeltaResponse getWeightDelta(Long userId) {
		return userService.getWeightDelta(userId);
	}

	@Override
	public GetWeeklyDailyNutrionIntakeRateResponse getWeeklyDailyNutrionIntakeRate(Long userId) {
		return mealService.getWeeklyDailyNutrionIntakeRate(userId);
	}

	@Override
	public GetIntakeOfDateResponse getDailyIntakeRelativeToRecommended(Long userId) {
		return mealService.getIntakeOfDate(userId, LocalDate.now());
	}

	@Override
	public GetDailyAverageSpendedCalorieResponse getDailyAverageSpendedCalorie(Long userId) {
		return exerciseService.getDailyAverageSpendedCalorie(userId);
	}

	@Override
	public GetWeeklyTotalDurationResponse getWeeklyTotalDuration(Long userId) {
		return exerciseService.getWeeklyTotalDuration(userId);
	}

	@Override
	public List<GetStreakRecordResponse> getStreakRecord(Long userId) {
		return badgeService.getStreakRecord(userId);
	}
}
