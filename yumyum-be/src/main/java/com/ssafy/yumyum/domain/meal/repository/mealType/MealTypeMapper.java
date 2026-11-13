package com.ssafy.yumyum.domain.meal.repository.mealType;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.yumyum.domain.meal.types.MealType;

@Mapper
public interface MealTypeMapper {
    List<MealTypeVO> findAll();
    Optional<MealTypeVO> findById(Long mealTypeId);
    int insert(MealTypeVO mealTypeVO);
    int updateById(MealTypeVO mealTypeVO);
    int deleteById(Long mealTypeId);
    int count();
	long maxId();
	Optional<MealTypeVO> findByMealTypeName(MealType mealTypeName);
}
