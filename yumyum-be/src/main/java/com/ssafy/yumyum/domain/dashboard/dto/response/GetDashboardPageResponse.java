package com.ssafy.yumyum.domain.dashboard.dto.response;

import java.util.List;

import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDashboardPageResponse {
	private List<GetBadgeListBadgeDTO> recentBadgeList;
	private GetWeightDeltaResponse weightDelta;
	private GetWeeklyDailyNutrionIntakeRateResponse weeklyDailyNutrionIntakeRate;
	private GetIntakeOfDateResponse dailyIntakeRelativeToRecommended;
	private GetDailyAverageSpendedCalorieResponse dailyAverageSpendedCalorie;
	private GetWeeklyTotalDurationResponse weeklyTotalDuration;
	private List<GetStreakRecordResponse> streakRecordList;
}
