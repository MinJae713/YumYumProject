package com.ssafy.yumyum.domain.meal.repository.mealItem;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MealItemMapper {
    List<MealItemVO> findAll();
    Optional<MealItemVO> findById(Long mealItemId);
    int insert(MealItemVO mealItemVO);
    int updateById(MealItemVO mealItemVO);
    int deleteById(Long mealItemId);
    int count();
	long maxId();
	List<MealItemVO> findAllByMealId(Long mealId);
	Optional<MealItemVO> findByMeallIdAndFoodId(Long mealId, Long foodId);
    int updateFoodId(@Param("realFoodId") Long realFoodId, @Param("tempFoodId") Long tempFoodId);
    List<Long> findMealIdsByFoodId(Long foodId);
}
