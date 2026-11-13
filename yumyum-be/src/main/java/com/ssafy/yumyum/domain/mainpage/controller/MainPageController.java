package com.ssafy.yumyum.domain.mainpage.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.badge.dto.response.GetStreakRecordResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.mainpage.dto.response.GetMainPageResponse;
import com.ssafy.yumyum.domain.mainpage.service.MainPageService;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;
import com.ssafy.yumyum.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainPageController {
	private final MainPageService mainPageService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<GetMainPageResponse>> getMainPage(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getMainPage(user.getUserId())));
	}

	@GetMapping("/recommend")
	public ResponseEntity<ApiResponse<List<GetRecommendMealResponse>>> getRecommendMeal(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getRecommendMeal(user.getUserId())));
	}

	@GetMapping("/meal")
	public ResponseEntity<ApiResponse<GetMealOfDateResponse>> getMealOfDate(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getMealOfDate(user.getUserId())));
	}

	@GetMapping("/exercise")
	public ResponseEntity<ApiResponse<GetExercisesOfDateResponse>> getExercisesOfDate(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getExercisesOfDate(user.getUserId())));
	}
	
	@GetMapping("/meal/intake")
	public ResponseEntity<ApiResponse<GetIntakeOfDateResponse>> getIntakeOfDate(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getIntakeOfDate(user.getUserId())));
	}
	
	@GetMapping("/badge/streak")
	public ResponseEntity<ApiResponse<List<GetStreakRecordResponse>>> getStreakRecord(
			@AuthenticationPrincipal UserPrincipal user) {
		return ResponseEntity.ok(ApiResponse.ok(mainPageService.getStreakRecord(user.getUserId())));
	}
}
