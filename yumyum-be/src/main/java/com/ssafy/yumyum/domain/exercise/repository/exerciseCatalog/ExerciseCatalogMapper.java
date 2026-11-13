package com.ssafy.yumyum.domain.exercise.repository.exerciseCatalog;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExerciseCatalogMapper {
    List<ExerciseCatalogVO> findAll();
    Optional<ExerciseCatalogVO> findById(Long exerciseId);
    int insert(ExerciseCatalogVO exerciseCatalogVO);
    int updateById(ExerciseCatalogVO exerciseCatalogVO);
    int deleteById(Long exerciseId);
    int count();
	long maxId();
	Optional<ExerciseCatalogVO> findByName(String name);
	List<ExerciseCatalogVO> findAllByExerciseCategoryId(Long exerciseCategoryId);
}
