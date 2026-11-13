package com.ssafy.yumyum.domain.dashboard.service;

import java.util.List;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.dashboard.dto.response.GetDashboardPageResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;

public interface DashboardService {
	GetDashboardPageResponse getDashboardPage(Long userId);
	List<GetBadgeListBadgeDTO> getRecentBadgeList(Long userId);
	GetWeightDeltaResponse getWeightDelta(Long userId); 
	GetWeeklyDailyNutrionIntakeRateResponse getWeeklyDailyNutrionIntakeRate(Long userId);
	GetIntakeOfDateResponse getDailyIntakeRelativeToRecommended(Long userId);
	GetDailyAverageSpendedCalorieResponse getDailyAverageSpendedCalorie(Long userId);
	GetWeeklyTotalDurationResponse getWeeklyTotalDuration(Long userId);
	List<GetStreakRecordResponse> getStreakRecord(Long userId);
}
