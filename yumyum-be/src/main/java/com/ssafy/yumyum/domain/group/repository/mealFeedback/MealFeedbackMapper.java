package com.ssafy.yumyum.domain.group.repository.mealFeedback;

import java.util.List;
import java.util.Optional;

import com.ssafy.yumyum.domain.group.dto.response.FeedbackResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MealFeedbackMapper {
    List<MealFeedbackVO> findAll();
    Optional<MealFeedbackVO> findById(Long mealFeedbackId);
    int insert(MealFeedbackVO mealFeedbackVO);
    int updateById(MealFeedbackVO mealFeedbackVO);
    int deleteById(Long mealFeedbackId);
    int count();
	long maxId();
    List<FeedbackResponse> findAllByGroupIdAndMealId(Long groupId, Long mealId);
    boolean existsByMealId(Long mealId);
    int deleteByGroupId(Long groupId);
}
