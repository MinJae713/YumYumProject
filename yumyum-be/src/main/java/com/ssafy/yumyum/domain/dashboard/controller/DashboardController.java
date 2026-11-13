package com.ssafy.yumyum.domain.dashboard.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.badge.service.dto.GetBadgeListBadgeDTO;
import com.ssafy.yumyum.domain.dashboard.dto.response.GetDashboardPageResponse;
import com.ssafy.yumyum.domain.dashboard.service.DashboardService;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.user.dto.response.GetWeightDeltaResponse;
import com.ssafy.yumyum.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
	private final DashboardService dashboardService;

	@GetMapping
	public ResponseEntity<ApiResponse<GetDashboardPageResponse>> getDashboardPage(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDashboardPage(user.getUserId())));
	}

	@GetMapping("/badge")
	public ResponseEntity<ApiResponse<List<GetBadgeListBadgeDTO>>> getRecentBadgeList(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getRecentBadgeList(user.getUserId())));
	}

	@GetMapping("/weight")
	public ResponseEntity<ApiResponse<GetWeightDeltaResponse>> getWeightDelta(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getWeightDelta(user.getUserId())));
	}

	@GetMapping("/nutrition")
	public ResponseEntity<ApiResponse<GetWeeklyDailyNutrionIntakeRateResponse>> getWeeklyDailyNutrionIntakeRate(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getWeeklyDailyNutrionIntakeRate(user.getUserId())));
	}

	@GetMapping("/nutrition/compare")
	public ResponseEntity<ApiResponse<GetIntakeOfDateResponse>> getDailyIntakeRelativeToRecommended(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDailyIntakeRelativeToRecommended(user.getUserId())));
	}

	@GetMapping("/exercise/average")
	public ResponseEntity<ApiResponse<GetDailyAverageSpendedCalorieResponse>> getDailyAverageSpendedCalorie(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDailyAverageSpendedCalorie(user.getUserId())));
	}

	@GetMapping("/exercise/time")
	public ResponseEntity<ApiResponse<GetWeeklyTotalDurationResponse>> getWeeklyTotalDuration(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getWeeklyTotalDuration(user.getUserId())));
	}

	@GetMapping("/streak")
	public ResponseEntity<ApiResponse<List<GetStreakRecordResponse>>> getStreakRecord(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(dashboardService.getStreakRecord(user.getUserId())));
	}
}
