package com.ssafy.yumyum.domain.meal.service;

import java.time.LocalDate;
import java.util.List;

import com.ssafy.yumyum.domain.meal.dto.request.AddMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.ApplyRecommendMealRequest;
import com.ssafy.yumyum.domain.meal.dto.request.TotalFoodRequest;
import com.ssafy.yumyum.domain.meal.dto.request.UpdateMealRequest;
import com.ssafy.yumyum.domain.meal.dto.response.AddMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.ApplyRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.DeleteMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetIntakeOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealDetailResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetMealOfDateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetRecommendMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetWeeklyDailyNutrionIntakeRateResponse;
import com.ssafy.yumyum.domain.meal.dto.response.SearchFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.TotalFoodResponse;
import com.ssafy.yumyum.domain.meal.dto.response.UpdateMealResponse;
import com.ssafy.yumyum.domain.meal.service.dto.MealInfoDTO;

public interface MealService {
	GetMealOfDateResponse getMealOfDate(Long userId, LocalDate mealDate);
	AddMealResponse addMeal(Long userId, AddMealRequest addMealRequest);
	GetMealDetailResponse getMealDetail(Long mealId);
	UpdateMealResponse updateMeal(UpdateMealRequest updateMealRequest);
	DeleteMealResponse deleteMeal(Long mealId);
	ApplyRecommendMealResponse applyRecommendMeal(ApplyRecommendMealRequest applyRecommendMealRequest);
	List<SearchFoodResponse> searchFood(String foodName);
	TotalFoodResponse totalFood(Long userId, TotalFoodRequest totalFoodRequest);
	List<GetRecommendMealResponse> getRecommendMeal(Long userId, LocalDate mealDate);
	List<LocalDate> getMonthRecord(Long userId, Integer year, Integer month);
	GetIntakeOfDateResponse getIntakeOfDate(Long userId, LocalDate mealDate);
	MealInfoDTO getMealInfo(Long mealId);
	
	GetWeeklyDailyNutrionIntakeRateResponse getWeeklyDailyNutrionIntakeRate(Long userId);
	List<LocalDate> getRecordedDate(Long userId);
}
