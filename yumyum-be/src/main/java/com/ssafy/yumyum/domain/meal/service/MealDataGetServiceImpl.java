package com.ssafy.yumyum.domain.meal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.yumyum.domain.meal.exception.NoFoodException;
import com.ssafy.yumyum.domain.meal.exception.NoMealCreatorTypeException;
import com.ssafy.yumyum.domain.meal.repository.food.FoodMapper;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;
import com.ssafy.yumyum.domain.meal.repository.meal.MealMapper;
import com.ssafy.yumyum.domain.meal.repository.meal.MealVO;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeMapper;
import com.ssafy.yumyum.domain.meal.repository.mealCreatorType.MealCreatorTypeVO;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemMapper;
import com.ssafy.yumyum.domain.meal.repository.mealItem.MealItemVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MealDataGetServiceImpl implements MealDataGetService {
	private final MealMapper mealMapper;
	private final MealCreatorTypeMapper mealCreatorTypeMapper;
	private final MealItemMapper mealItemMapper;
	private final FoodMapper foodMapper;

	@Override
	public List<MealVO> findMealByUserId(Long userId) {
		return mealMapper.findAllByUserId(userId);
	}

	@Override
	public MealCreatorTypeVO findMealCreatorTypeById(Long mealCreatorTypeId) {
		return mealCreatorTypeMapper.findById(mealCreatorTypeId)
				.orElseThrow(() -> new NoMealCreatorTypeException());
	}

	@Override
	public List<MealItemVO> findMealItemByMealId(Long mealId) {
		return mealItemMapper.findAllByMealId(mealId);
	}

	@Override
	public FoodVO findFoodById(Long foodId) {
		return foodMapper.findById(foodId)
				.orElseThrow(() -> new NoFoodException());
	}

}
