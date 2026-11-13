package com.ssafy.yumyum.domain.meal.service;

import java.util.List;

import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;

public interface MealDataGetService {
	List<MealVO> findMealByUserId(Long userId);
	MealCreatorTypeVO findMealCreatorTypeById(Long mealCreatorTypeId);
	List<MealItemVO> findMealItemByMealId(Long mealId);
	FoodVO findFoodById(Long foodId);
}
