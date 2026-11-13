package com.ssafy.yumyum.domain.exercise.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.yumyum.domain.exercise.dto.request.AddExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.ScoreExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.request.UpdateExerciseRequest;
import com.ssafy.yumyum.domain.exercise.dto.response.GetDailyAverageSpendedCalorieResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseDetailResponse.ExerciseCategoryInfo;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExerciseListResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.GetExercisesOfDateResponse.GetExercisesOfDateExercise;
import com.ssafy.yumyum.domain.exercise.dto.response.GetWeeklyTotalDurationResponse;
import com.ssafy.yumyum.domain.exercise.dto.response.ScoreExerciseResponse;

@SpringBootTest
public class ExerciseServiceTest {
	@Autowired
	private ExerciseService exerciseService;

	@Test
	void getExercisesOfDate() {
		Long userId = 5L;
		LocalDate exerciseDate = LocalDate.of(1999, 7, 13);
		GetExercisesOfDateResponse getExercisesOfDateResponse = 
				exerciseService.getExercisesOfDate(userId, exerciseDate);
		StringBuffer buffer = new StringBuffer("============================\n");
		exerciseDate = getExercisesOfDateResponse.getExerciseDate();
		buffer.append(String.format("exerciseDate: %s\n", exerciseDate.toString()));
		Double totalCalories = getExercisesOfDateResponse.getTotalCalories();
		buffer.append(String.format("totalCalories: %f\n", totalCalories));
		Integer totalPoints = getExercisesOfDateResponse.getTotalPoints();
		buffer.append(String.format("totalPoints: %d\n", totalPoints));
		List<GetExercisesOfDateExercise> exercises = getExercisesOfDateResponse.getExercises();
		buffer.append(String.format("exercises: [\n"));
		exercises.forEach(exercise -> {
			buffer.append("\t{\n");
			Long exerciseRecordId = exercise.getExerciseRecordId();
			buffer.append(String.format("\t\texerciseRecordId: %d\n", exerciseRecordId));
			String exerciseName = exercise.getExerciseName();
			buffer.append(String.format("\t\texerciseName: %s\n", exerciseName));
			String exerciseCategoryName = exercise.getExerciseCategoryName();
			buffer.append(String.format("\t\texerciseCategoryName: %s\n", exerciseCategoryName));
			Double calories = exercise.getCalories();
			buffer.append(String.format("\t\tcalories: %f\n", calories));
			Integer points = exercise.getPoints();
			buffer.append(String.format("\t\tpoints: %d\n", points));
			String exerciseOptionDetail = exercise.getExerciseOptionDetail();
			buffer.append(String.format("\t\texerciseOptionDetail: %s\n", exerciseOptionDetail));
			buffer.append("\t}\n");
		});
		buffer.append("]");
		System.out.println(buffer);
	}
	
	@Test
	void getExerciseDetail() {
		Long exerciseRecordId = 21L;
		GetExerciseDetailResponse getExerciseDetailResponse = 
				exerciseService.getExerciseDetail(exerciseRecordId);
	    StringBuffer buffer = new StringBuffer("============================\n");
	    // exerciseCategoryList
	    buffer.append("exerciseCategoryList: [\n");
	    List<ExerciseCategoryInfo> exerciseCategoryList = 
	    		getExerciseDetailResponse.getExerciseCategoryList();
	    exerciseCategoryList.forEach(exerciseCategory -> {
	        buffer.append("\t{\n");
	        buffer.append(String.format("\t\texerciseCategoryId: %d\n", 
	        		exerciseCategory.getExerciseCategoryId()));
	        buffer.append(String.format("\t\texerciseCategoryName: %s\n", 
	        		exerciseCategory.getExerciseCategoryName()));
	        buffer.append("\t}\n");
	    });
	    buffer.append("]\n");

	    buffer.append(String.format("exerciseCategoryIndex: %d\n", 
	    		getExerciseDetailResponse.getExerciseCategoryIndex()));

	    // exerciseList
	    buffer.append("exerciseList: [\n");
	    List<GetExerciseListResponse> exerciseList = 
	    		getExerciseDetailResponse.getExerciseList();
	    exerciseList.forEach(exercise -> {
	        buffer.append("\t{\n");
	        buffer.append(String.format("\t\texerciseId: %d\n", 
	        		exercise.getExerciseId()));
	        buffer.append(String.format("\t\texerciseName: %s\n", 
	        		exercise.getExerciseName()));
	        buffer.append("\t}\n");
	    });
	    buffer.append("]\n");

	    buffer.append(String.format("exerciseIndex: %d\n", 
	    		getExerciseDetailResponse.getExerciseIndex()));
	    buffer.append(String.format("calories: %f\n", 
	    		getExerciseDetailResponse.getCalories()));
	    buffer.append(String.format("points: %d\n", 
	    		getExerciseDetailResponse.getPoints()));
	    
	    Integer setNo = getExerciseDetailResponse.getSetNo();
	    buffer.append(String.format("setNo: %s\n", 
	    		setNo == null ? "null" : setNo.toString()));
	    Integer reps = getExerciseDetailResponse.getReps();
	    buffer.append(String.format("reps: %s\n", 
	    		reps == null ? "null" : reps.toString()));
	    Integer weightKg = getExerciseDetailResponse.getWeightKg();
	    buffer.append(String.format("weightKg: %s\n", 
	    		weightKg == null ? "null" : weightKg.toString()));
	    Integer durationMin = getExerciseDetailResponse.getDurationMin();
	    buffer.append(String.format("durationMin: %s\n", 
	    		durationMin == null ? "null" : durationMin.toString()));
	    System.out.println(buffer);
	}

	@Test
	void getExerciseList() {
		Long exerciseCategoryId = 6L;
		List<GetExerciseListResponse> getExerciseListResponses = 
				exerciseService.getExerciseList(exerciseCategoryId);
		StringBuffer buffer = new StringBuffer("============================\n");

	    getExerciseListResponses.forEach(getExerciseListResponse -> {
	        buffer.append("{\n");
	        buffer.append(String.format("\texerciseId: %d\n",
	        		getExerciseListResponse.getExerciseId()));
	        buffer.append(String.format("\texerciseName: %s\n", 
	        		getExerciseListResponse.getExerciseName()));
	        buffer.append("}\n");
	    });

	    System.out.println(buffer);
	}

	@Test
	void addExercise() {
		AddExerciseRequest addExerciseRequest = 
				AddExerciseRequest.builder()
				.exerciseDate(LocalDate.now().minusDays(1))
				.exerciseId(12L)
				.calories(100.0)
				.points(45)
				.setNo(3)
				.reps(15)
				.weightKg(70)
				.durationMin(40)
				.build();
		exerciseService.addExercise(11L, addExerciseRequest);
	}

	@Test
	void updateExercise() {
		UpdateExerciseRequest updateExerciseRequest = 
				UpdateExerciseRequest.builder()
				.exerciseRecordId(21L)
				.exerciseId(5L)
				.calories(80.0)
				.points(12)
				.setNo(null)
				.reps(null)
				.weightKg(0)
				.durationMin(23)
				.build();
		exerciseService.updateExercise(updateExerciseRequest);
	}

	@Test
	void deleteExercise() {
		Long exerciseRecordId = 21L;
		exerciseService.deleteExercise(exerciseRecordId);
	}

	@Test
	void scoreExercise() {
		ScoreExerciseRequest scoreExerciseRequest = 
				ScoreExerciseRequest.builder()
				.exerciseId(8L)
				.durationMin(65)
				.build();
		ScoreExerciseResponse scoreExerciseResponse = 
				exerciseService.scoreExercise(5L, scoreExerciseRequest);
		StringBuffer buffer = new StringBuffer("============================\n");
		buffer.append(String.format("calories: %f\n", scoreExerciseResponse.getCalories()));
		buffer.append(String.format("points: %d\n", scoreExerciseResponse.getPoints()));
		System.out.println(buffer);
	}
	@Test
	void getMonthRecord() {
		List<LocalDate> getMonthRecordResponse = 
				exerciseService.getMonthRecord(11L, 2025, 11);
		getMonthRecordResponse.forEach(System.out::println);
	}
	@Test
	void getDailyAverageSpendedCalorie() {
	    Long userId = 11L;

	    GetDailyAverageSpendedCalorieResponse res =
	            exerciseService.getDailyAverageSpendedCalorie(userId);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append(String.format("spendedAverageCalorie: %f\n",
	            res.getSpendedAverageCalorie()));
	    buffer.append(String.format("lastWeekSpendedAverageCalorie: %f\n",
	            res.getLastWeekSpendedAverageCalorie()));
	    buffer.append(String.format("percentFromLastWeek: %f\n",
	            res.getPercentFromLastWeek()));

	    System.out.println(buffer);
	}
	@Test
	void getWeeklyTotalDuration() {
	    Long userId = 11L;

	    GetWeeklyTotalDurationResponse res =
	            exerciseService.getWeeklyTotalDuration(userId);

	    StringBuffer buffer = new StringBuffer("============================\n");
	    buffer.append(String.format("exerciseTimeSameWeek: %d\n", res.getExerciseTimeSameWeek()));
	    buffer.append(String.format("exerciseTimeLastWeek: %d\n", res.getExerciseTimeLastWeek()));
	    buffer.append(String.format("percentFromLastWeek: %f\n",
	            res.getPercentFromLastWeek()));

	    System.out.println(buffer);
	}

}
