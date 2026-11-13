package com.ssafy.yumyum.domain.exercise.service;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.yumyum.domain.exercise.dto.request.AddExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.ScoreExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.UpdateExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.response.AddExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.DeleteExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseListResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.ScoreExerciseResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.UpdateExerciseResponse;
import com.ssafy.yumyum.domain.exercise.service.dto.ExerciseRecordInfoDTO;

public interface ExerciseService {
	GetExercisesOfDateResponse getExercisesOfDate(Long userId, LocalDate exerciseDate);
	GetExerciseDetailResponse getExerciseDetail(Long exerciseRecordId);
	List<GetExerciseListResponse> getExerciseList(Long exerciseCategoryId);
	AddExerciseResponse addExercise(Long userId, AddExerciseRequest addExerciseRequest);
	UpdateExerciseResponse updateExercise(UpdateExerciseRequest updateExerciseRequest);
	DeleteExerciseResponse deleteExercise(Long exerciseRecordId);
	ScoreExerciseResponse scoreExercise(Long userId, ScoreExerciseRequest scoreExerciseRequest);
	List<LocalDate> getMonthRecord(Long userId, Integer year, Integer month);
	ExerciseRecordInfoDTO getExerciseRecordInfo(Long exerciseRecordId);
	
	GetDailyAverageSpendedCalorieResponse getDailyAverageSpendedCalorie(Long userId);
	GetWeeklyTotalDurationResponse getWeeklyTotalDuration(Long userId);
	List<LocalDate> getRecordedDates(Long userId);
}
