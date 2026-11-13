package com.ssafy.yumyum.domain.meal.repository.groupMeal;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMealMapper {
	List<GroupMealVO> findAll();
	Optional<GroupMealVO> findById(Long mealId);
	int insert(GroupMealVO groupMealVO);
	int updateById(GroupMealVO groupMealVO);
	int deleteById(Long mealId);
	int count();
	long maxId();
}
