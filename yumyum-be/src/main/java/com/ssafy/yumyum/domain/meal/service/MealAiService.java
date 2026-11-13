package com.ssafy.yumyum.domain.meal.service;

import com.ssafy.yumyum.domain.ai.dto.response.AiMealResponse;
import com.ssafy.yumyum.domain.meal.dto.response.GetAiMealListResponse;

import java.time.LocalDate;
import java.util.List;

public interface MealAiService {

    void saveAiMealList(Long userId, List<AiMealResponse.Recommendation> recommendations);
    GetAiMealListResponse getAiMealList(Long userId, LocalDate date);
}
