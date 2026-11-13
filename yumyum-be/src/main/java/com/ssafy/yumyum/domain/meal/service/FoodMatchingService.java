package com.ssafy.yumyum.domain.meal.service;

import com.ssafy.yumyum.domain.meal.dto.request.FoodMatchRequest;
import com.ssafy.yumyum.domain.meal.dto.response.FoodMatchResponse;
import com.ssafy.yumyum.domain.meal.dto.response.FoodSearchPageResponse;
import com.ssafy.yumyum.domain.meal.repository.food.FoodVO;

import java.util.List;

public interface FoodMatchingService {

    FoodVO findOrRegisterTempFood(String aiFoodName);

    void saveCache(String name, Long id, String status);

    List<FoodMatchResponse> getPendingList();

    FoodSearchPageResponse searchFoodList(String keyword, int page, int size);

    int matchFoods(List<FoodMatchRequest> foodMatchRequestList);
}
