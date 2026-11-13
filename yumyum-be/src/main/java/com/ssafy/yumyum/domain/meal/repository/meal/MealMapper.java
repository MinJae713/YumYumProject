package com.ssafy.yumyum.domain.meal.repository.meal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MealMapper {
    List<MealVO> findAll();
    Optional<MealVO> findById(Long mealId);
    int insert(MealVO mealVO);
    int updateById(MealVO mealVO);
    int deleteById(Long mealId);
    int count();
	long maxId();
	List<MealVO> findAllByUserIdAndMealDate(Long userId, LocalDate mealDate);
	List<LocalDate> findMealDateByUserIdAndMealYearAndMealMonth(Long userId, Integer year, Integer month);
	List<MealVO> findAllByUserId(Long userId);
	List<MealVO> findAllByUserIdAndMealDateBetweenStartDateAndEndDate(Long userId, LocalDate startDate, LocalDate endDate);

    // [Admin] AI 식단 목록 조회 (페이징)
    List<MealVO> findAllAiMealsForAdmin(@Param("limit") int limit, @Param("offset") int offset);

    // [Admin] AI 식단 전체 개수 조회
    int countAllAiMealsForAdmin();
}
