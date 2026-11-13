package com.ssafy.yumyum.domain.meal.repository.mealAiDetail;

import com.ssafy.yumyum.domain.meal.service.dto.AiMealHeaderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MealAiDetailMapper {

    int insert(MealAiDetailVO mealAiDetailVO);

    Optional<MealAiDetailVO> findByMealId(Long mealId);

    int updateById(MealAiDetailVO mealAiDetailVO);

    int deleteById(Long mealId);

    List<AiMealHeaderDTO> findAiMealListByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
