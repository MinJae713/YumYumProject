package com.ssafy.yumyum.domain.exercise.repository.exerciseCategory;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExerciseCategoryMapper {
    List<ExerciseCategoryVO> findAll();
    Optional<ExerciseCategoryVO> findById(Long exerciseCategoryId);
    int insert(ExerciseCategoryVO exerciseCategoryVO);
    int updateById(ExerciseCategoryVO exerciseCategoryVO);
    int deleteById(Long exerciseCategoryId);
    int count();
	long maxId();
	Optional<Long> findIdByCategoryName(String categoryName);
}
