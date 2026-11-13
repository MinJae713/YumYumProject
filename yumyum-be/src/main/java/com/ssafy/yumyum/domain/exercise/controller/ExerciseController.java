package com.ssafy.yumyum.domain.exercise.controller;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.yumyum.common.controller.ApiResponse;
import com.ssafy.yumyum.domain.exercise.dto.request.AddExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.ScoreExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.UpdateExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseListResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.ScoreExerciseResponse;
import com.ssafy.yumyum.domain.exercise.service.ExerciseService;
import com.ssafy.yumyum.security.UserPrincipal;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseController {
	private final ExerciseService exerciseService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<GetExercisesOfDateResponse>> getExercisesOfDate(
			@AuthenticationPrincipal UserPrincipal user,
			@RequestParam @PastOrPresent(message = "유효한 날짜를 입력하세요") LocalDate exerciseDate) {
		return ResponseEntity.ok(ApiResponse.ok(exerciseService.getExercisesOfDate(user.getUserId(), exerciseDate)));
	}
	
	@GetMapping("/{exerciseRecordId}")
	public ResponseEntity<ApiResponse<GetExerciseDetailResponse>> getExerciseDetail(
			@AuthenticationPrincipal UserPrincipal user,
			@PathVariable @Positive(message = "운동 기록 아이디는 양수여야 합니다.") Long exerciseRecordId) {
		return ResponseEntity.ok(ApiResponse.ok(exerciseService.getExerciseDetail(exerciseRecordId)));
	}
	
	@GetMapping("/list/{exerciseCategoryId}")
	public ResponseEntity<ApiResponse<List<GetExerciseListResponse>>> getExerciseList(
			@AuthenticationPrincipal UserPrincipal user,
			@PathVariable @Positive(message = "운동 카테고리 아이디는 양수여야 합니다.") Long exerciseCategoryId) {
		return ResponseEntity.ok(ApiResponse.ok(exerciseService.getExerciseList(exerciseCategoryId)));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<String>> addExercise(
			@AuthenticationPrincipal UserPrincipal user,
			@Validated @RequestBody AddExerciseRequest addExerciseRequest) {
		exerciseService.addExercise(user.getUserId(), addExerciseRequest);
		return ResponseEntity.ok(ApiResponse.ok("추가 성공"));
	}
	
	@PatchMapping
	public ResponseEntity<ApiResponse<String>> updateExercise(
			@AuthenticationPrincipal UserPrincipal user,
			@Validated @RequestBody UpdateExerciseRequest updateExerciseRequest) {
		exerciseService.updateExercise(updateExerciseRequest);
		return ResponseEntity.ok(ApiResponse.ok("수정 성공"));
	}
	
	@DeleteMapping("/{exerciseRecordId}")
	public ResponseEntity<ApiResponse<String>> deleteExercise(
			@AuthenticationPrincipal UserPrincipal user,
			@PathVariable @Positive(message = "운동 기록 아이디는 양수여야 합니다.") Long exerciseRecordId) {
		exerciseService.deleteExercise(exerciseRecordId);
		return ResponseEntity.ok(ApiResponse.ok("삭제 성공"));
	}
	
	@PostMapping("/score")
	public ResponseEntity<ApiResponse<ScoreExerciseResponse>> scoreExercise(
			@AuthenticationPrincipal UserPrincipal user,
			@Validated @RequestBody ScoreExerciseRequest scoreExerciseRequest) {
		return ResponseEntity.ok(ApiResponse.ok(exerciseService.scoreExercise(user.getUserId(), scoreExerciseRequest)));
	}
	
	@GetMapping("/month-record")
	public ResponseEntity<ApiResponse<List<LocalDate>>> getMonthRecord(
			@AuthenticationPrincipal UserPrincipal user,
			@RequestParam 
			@Range(min = 1900, max = 2100, message = "최소 1900년, 최대 2100년 사이만 가능합니다.")
			Integer year,
			@RequestParam 
			@Range(min = 1, max = 12, message = "최소 1월, 최대 12월 사이만 가능합니다.")
			Integer month) {
		return ResponseEntity.ok(ApiResponse.ok(exerciseService.getMonthRecord(user.getUserId(), year, month)));
	}
}
