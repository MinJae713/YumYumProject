package com.ssafy.yumyum.domain.meal.repository.mealCreatorType;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MealCreatorTypeMapper {
    List<MealCreatorTypeVO> findAll();
    Optional<MealCreatorTypeVO> findById(Long mealCreatorTypeId);
    int insert(MealCreatorTypeVO mealCreatorTypeVO);
    int updateById(MealCreatorTypeVO mealCreatorTypeVO);
    int deleteById(Long mealCreatorTypeId);
    int count();
	long maxId();
	Optional<MealCreatorTypeVO> findByMealCreatorTypeName(String mealCreatorTypeName);
}
